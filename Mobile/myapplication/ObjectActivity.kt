package com.example.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.Datasource
import com.example.myapplication.db.Helper
import com.example.myapplication.model.MyObject


class ObjectActivity : AppCompatActivity() {
    private lateinit var readDb: SQLiteDatabase
    private lateinit var writeDb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object)
        println("GOT HERE")
        val dbHelper = Helper(this)
        val dataSource = Datasource.getInstance(
            dbHelper.readableDatabase,
            dbHelper.writableDatabase
        )
        val bundle: Bundle? = intent.extras
        var id = 0;
        if (bundle != null) {
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                findViewById<EditText>(R.id.editName).setText(bundle.getString("MainActName"))
                findViewById<EditText>(R.id.editDescription).setText(bundle.getString("MainActDescription"))
                findViewById<EditText>(R.id.editPrice).setText(bundle.getString("MainActPrice"))
                findViewById<EditText>(R.id.editQuantity).setText(bundle.getString("MainActQuantity"))
                findViewById<Button>(R.id.btnAdd).setText("Update")
            }
        }
        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val objId: Int? = if (id == 0) {
                null;
            }else {
                id;
            }
            val obj = MyObject(
                objId,
                findViewById<EditText>(R.id.editName).text.toString(),
                findViewById<EditText>(R.id.editDescription).text.toString(),
                findViewById<EditText>(R.id.editPrice).text.toString().toInt(),
                findViewById<EditText>(R.id.editQuantity).text.toString().toInt()
            )
            if (objId == null) {
                obj.id = dataSource?.add(obj)?.toInt()
            }else{
                dataSource?.update(obj)
            }

            intent.putExtra("object", obj);
            setResult(RESULT_OK, intent)
            finish()

        }
    }
}