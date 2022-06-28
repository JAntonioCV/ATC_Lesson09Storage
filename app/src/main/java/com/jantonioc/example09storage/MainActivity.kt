package com.jantonioc.example09storage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var etSharedPref : EditText
    private lateinit var etInternal : EditText
    private lateinit var etExternal : EditText

    private lateinit var tvResultSP : TextView
    private lateinit var btnSaveSP : Button

    private lateinit var tvResultIS : TextView
    private lateinit var btnSaveIS : Button
    private lateinit var btnReadIS : Button

    private lateinit var tvResultES : TextView
    private lateinit var btnSaveES : Button
    private lateinit var btnReadES : Button


    private lateinit var mySharedPreferences: SharedPreferences
    private val PREF_NAME = "MySharedPreferencesFile"

    private val INTERNAL_FILE_NAME = "MyInternalFile"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etSharedPref = findViewById(R.id.etSharedPref)
        etInternal = findViewById(R.id.etInternal)
        etExternal = findViewById(R.id.etExternal)

        tvResultSP = findViewById(R.id.tvResultSharedPref)
        tvResultIS = findViewById(R.id.tvResultIS)
        tvResultES = findViewById(R.id.tvResultES)

        btnSaveSP = findViewById(R.id.btnSaveSharedPref)
        btnSaveIS = findViewById(R.id.btnSaveIS)
        btnSaveES = findViewById(R.id.btnSaveES)

        btnReadIS = findViewById(R.id.btnLoadIS)
        btnReadES = findViewById(R.id.btnLoadES)

        mySharedPreferences = getSharedPreferences(PREF_NAME, 0)

        btnSaveSP.setOnClickListener {
            saveToSharedPreferenes()
        }

        btnSaveIS.setOnClickListener {
            savetoInternalStorageFile()
        }

        btnReadIS.setOnClickListener {
            loadFromInternalStorageFile()
        }

        btnSaveES.setOnClickListener {
            savetoExternalStorageFile()
        }

        btnReadES.setOnClickListener {
            readFromExternalFile()
        }

        val readSharedPref : SharedPreferences = getSharedPreferences(PREF_NAME, 0)
        if (readSharedPref.contains("KEY")){
            tvResultSP.text = readSharedPref.getString("KEY", "No encontrada")
        }



    }

    fun readFromExternalFile(){
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        var file = File(folder, "ExternalFile.txt")
        var fileInputStream : FileInputStream? = null

        try {
            fileInputStream = FileInputStream(file)
            var buffer = StringBuffer()
            var i = -1
            while ( fileInputStream.read().also { i = it } != -1){
                buffer.append(i.toChar())
            }
            tvResultES.text = buffer.toString()

        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            if (fileInputStream != null){
                fileInputStream.close()
            }
        }
    }

    private fun savetoExternalStorageFile() {


        if ( etExternal.text.toString().isNullOrEmpty() ){
            Toast.makeText(this, "Favor digite un dato", Toast.LENGTH_LONG).show()
            return
        }

        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){

            val text = etExternal.text.toString()
            val folder : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(folder, "ExternalFile.txt")

            var fileOutputStream : FileOutputStream? = null

            try {

                fileOutputStream  = FileOutputStream(file)
                fileOutputStream.write( text.toByteArray() )
                Toast.makeText(this, "Guardo", Toast.LENGTH_LONG).show()

            } catch (e: Exception){
                e.printStackTrace()
            } finally {
                if (fileOutputStream != null){
                    fileOutputStream.close()
                }
            }
        }
        else {
            Toast.makeText(this, "Almacenamiento Externo NO montado", Toast.LENGTH_LONG).show()
        }

    }

    private fun loadFromInternalStorageFile() {
        try {
            val file = openFileInput(INTERNAL_FILE_NAME)
            var temp = ""
            var bytes: ByteArray = file.readBytes()
            for (byte in bytes)
            {
                temp+= byte.toInt().toChar()
            }

            tvResultIS.text = temp
            Toast.makeText(this,"File Readed",Toast.LENGTH_SHORT).show()

        }catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun savetoInternalStorageFile() {
        if(etInternal.text.isNullOrEmpty())
        {
            Toast.makeText(this,"Debe digirtar el texto a guardar",Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val fileOutputStream: FileOutputStream = openFileOutput(INTERNAL_FILE_NAME, Context.MODE_PRIVATE)
            val text = etInternal.text.toString()
            fileOutputStream.write(text.toByteArray(Charsets.UTF_8))
            fileOutputStream.close()
            etInternal.text.clear()
            Toast.makeText(this,"Almacenado con exito",Toast.LENGTH_SHORT).show()

        }catch (e:Exception)
        {
            e.printStackTrace()
        }

    }

    private fun saveToSharedPreferenes() {
        if(etSharedPref.text.isNullOrEmpty())
        {
            Toast.makeText(this,"Debe digirtar el texto a guardar",Toast.LENGTH_SHORT).show()
            return
        }
        val editor: SharedPreferences.Editor = mySharedPreferences.edit()
        val text  = etSharedPref.text.toString()
        editor.putString("KEY",text)
        editor.apply()

        etSharedPref.text.clear()
        Toast.makeText(this,"Almacenado con exito",Toast.LENGTH_SHORT).show()


    }
}