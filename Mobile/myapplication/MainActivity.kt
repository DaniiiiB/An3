package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.data.Datasource
import com.example.myapplication.db.Helper
import com.example.myapplication.model.MyObject



private const val REQUEST_CODE_ADD : Int = 2307;
private const val REQUEST_CODE_UPDATE : Int = 2308;



class MainActivity : AppCompatActivity() {
    private var listObjects = ArrayList<MyObject>()
    private lateinit var readDb: SQLiteDatabase
    private lateinit var writeDb: SQLiteDatabase
    private lateinit var objectsAdapter: ObjectsAdapter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHelper = Helper(this)
        readDb = dbHelper.readableDatabase
        writeDb = dbHelper.writableDatabase
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addObject -> {
                val intent = Intent(this, ObjectActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(REQUEST_CODE_ADD == requestCode && resultCode == Activity.RESULT_OK) {
            val newObject: MyObject = data?.getSerializableExtra("object") as MyObject
            listObjects.add(newObject)
        }

        else if(REQUEST_CODE_UPDATE == requestCode && resultCode == Activity.RESULT_OK) {
            val newObject: MyObject = data?.getSerializableExtra("object") as MyObject
            for (i in 0 until listObjects.size) {
                val existingObject = listObjects[i]
                if (existingObject.id == newObject.id) {
                    listObjects[i] = newObject;
                    break;
                }
            }
        }
        objectsAdapter.notifyDataSetChanged()
    }

    fun loadData() {
        listObjects = Datasource.getInstance(readDb, writeDb)?.loadMyObjects() as ArrayList<MyObject>
        val lvObjects: ListView = findViewById(R.id.lvMyObjects)
        objectsAdapter = ObjectsAdapter(this, listObjects)
        lvObjects.adapter = objectsAdapter
    }

    inner class ObjectsAdapter(context: Context, private var objectList: ArrayList<MyObject>) :
        BaseAdapter() {
        private var context: Context? = context
        override fun getCount(): Int {
            return objectList.size
        }

        override fun getItem(position: Int): Any {
            return objectList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder
            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.list_item, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }
            val obj = objectList[position]
            vh.textViewName.text = obj.name
            vh.textViewDescription.text = "Description: ${obj.description}"
            vh.textViewPrice.text = "Price: ${obj.price}"
            vh.textViewQuantity.text = "Quantity: ${obj.quantity}"
            vh.ivEdit.setOnClickListener {
                updateObject(obj)
            }

            vh.ivDelete.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Are you sure you want to delete?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { _, _ ->
                        println("remove at pos ${position}, obj: $obj}")
                        println(listObjects)
                        Datasource.getInstance(readDb, writeDb)?.delete(obj.id!!)
                        listObjects.removeAt(position)
                        println(listObjects)
                        objectsAdapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
            return view
        }
    }

    private fun updateObject(obj: MyObject) {
        val intent = Intent(this, ObjectActivity::class.java)
        intent.putExtra("MainActId", obj.id)
        intent.putExtra("MainActName", obj.name)
        intent.putExtra("MainActDescription", obj.description)
        intent.putExtra("MainActPrice", obj.price.toString())
        intent.putExtra("MainActQuantity", obj.quantity.toString())
        startActivityForResult(intent, REQUEST_CODE_UPDATE)

    }

    class ViewHolder(private val view: View?) {
        val textViewName: TextView = view?.findViewById(R.id.item_name) as TextView
        val textViewDescription: TextView = view?.findViewById(R.id.item_description) as TextView
        val textViewPrice: TextView = view?.findViewById(R.id.item_price) as TextView
        val textViewQuantity: TextView = view?.findViewById(R.id.item_quantity) as TextView
        val ivEdit: ImageView = view?.findViewById(R.id.ivEdit) as ImageView
        val ivDelete: ImageView = view?.findViewById(R.id.ivDelete) as ImageView
    }
}