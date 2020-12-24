package com.example.newtodo

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var arrayList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private val sharedPrefs = "sharedPrefs"
    private val stringKey = "KEY"
    private lateinit var listAsString: String
    private lateinit var editor: SharedPreferences.Editor

    private val defValue = "657d8fsxa09/ds"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("elene", "onCreate")
    }

    override fun onResume() {
        super.onResume()
        loadData()
        Log.i("elene", "onResume")
    }

    override fun onPause() {
        super.onPause()
        saveData()
        Log.i("elene", "onPause")
    }

    private fun addNewTask() {
        val newTask = findViewById<EditText>(R.id.editText).text.toString()
        arrayList.add(newTask)
        adapter.notifyDataSetChanged()
        findViewById<EditText>(R.id.editText).setText("")
        Log.i("elene", arrayList.joinToString(separator = "^"))
    }

    private fun removeTask(index: Int) {
        arrayList.removeAt(index)
        adapter.notifyDataSetChanged()
        Log.i("elene", arrayList.joinToString(separator = "^"))
    }

    private fun setup() {
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList)
        findViewById<ListView>(R.id.listView).adapter = adapter

        findViewById<Button>(R.id.button).isEnabled = findViewById<EditText>(R.id.editText).text.trim().isNotEmpty()

        findViewById<EditText>(R.id.editText).doOnTextChanged { _, _, _, _ ->
            findViewById<Button>(R.id.button).isEnabled =
                findViewById<EditText>(R.id.editText).text.toString().trim().isNotEmpty()
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            addNewTask()
        }

        findViewById<ListView>(R.id.listView).setOnItemClickListener { _, _, position, _ ->
            removeTask(position)
        }

        adapter.notifyDataSetChanged()
    }

    private fun saveData() {
        listAsString = arrayList.joinToString(separator = "^")
        val sharedPrefs = getSharedPreferences(sharedPrefs, MODE_PRIVATE)
        editor = sharedPrefs.edit()
        editor.putString(stringKey, listAsString)
        editor.apply()
        Log.i("elene", "sharedlistis string $listAsString")
    }

    private fun loadData() {
        val sharedPrefs = getSharedPreferences(sharedPrefs, MODE_PRIVATE)
        val newString = sharedPrefs.getString(stringKey, defValue)
        if (newString.equals(defValue)) {
            setup()
        } else {
            arrayList = newString?.trim()?.split("^")?.toCollection(ArrayList<String>())!!
            if (arrayList.get(0) == "") {
                arrayList.removeAt(0)
            }
            setup()
            Log.i("elene", "LOADDATA ARRAYLIST is $arrayList and size ${arrayList.size}")
        }
    }
}
