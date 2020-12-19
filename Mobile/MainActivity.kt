package ubb.barcoaie.lab4_ma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import ubb.barcoaie.lab4_ma.Activities.Create.CreateActivity
import ubb.barcoaie.lab4_ma.Activities.Delete.DeleteActivity
import ubb.barcoaie.lab4_ma.Activities.Read.ReadActivity
import ubb.barcoaie.lab4_ma.Activities.Update.UpdateActivity
import ubb.barcoaie.lab4_ma.Model.Vegetable

class MainActivity : AppCompatActivity() {
    private val VEGETABLE_CREATE_REQUEST_CODE = 1
    private val VEGETABLE_READ_REQUEST_CODE = 2
    private val VEGETABLE_UPDATE_REQUEST_CODE = 3
    private val VEGETABLE_DELETE_REQUEST_CODE = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_read: Button = this.findViewById(R.id.read_vegetables_button)
        button_read.setOnClickListener {
            val intent = Intent(this, ReadActivity::class.java)
            startActivityForResult(intent, VEGETABLE_READ_REQUEST_CODE)
        }

        val button_delete: Button = this.findViewById(R.id.delete_vegetables_button)
        button_delete.setOnClickListener {
            val intent = Intent(this, DeleteActivity::class.java)
            startActivityForResult(intent, VEGETABLE_DELETE_REQUEST_CODE)
        }

        val button_create: Button = this.findViewById(R.id.create_vegetables_button)
        button_create.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, VEGETABLE_CREATE_REQUEST_CODE)
        }

        val button_update: Button = this.findViewById(R.id.update_vegetables_button)
        button_update.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            startActivityForResult(intent, VEGETABLE_UPDATE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VEGETABLE_CREATE_REQUEST_CODE && resultCode == RESULT_OK) {
            val vegetable: Vegetable = data?.extras?.get("add") as Vegetable
            Toast.makeText(this, "back to menu from create with $vegetable",Toast.LENGTH_LONG).show()
        }
        else if (requestCode == VEGETABLE_READ_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this,"back to menu from read",Toast.LENGTH_LONG).show()
        }
        else if (requestCode == VEGETABLE_UPDATE_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this,"back to menu from update",Toast.LENGTH_LONG).show()
        }
        else if (requestCode == VEGETABLE_DELETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this,"back to menu from delete",Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(this, "result was not ok", Toast.LENGTH_LONG).show()
    }
}