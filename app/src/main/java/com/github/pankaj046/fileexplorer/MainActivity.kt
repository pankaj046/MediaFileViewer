package com.github.pankaj046.fileexplorer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.pankaj046.library.FileClickListener
import com.github.pankaj046.library.FileExplorer
import java.io.File

class MainActivity : AppCompatActivity() {

    private var fileExplorer: FileExplorer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fileExplorer = findViewById<FileExplorer>(R.id.file_explorer)
        fileExplorer?.setListener(object : FileClickListener {
            override fun onClick(file: File?) {
                Toast.makeText(this@MainActivity, file?.name, Toast.LENGTH_SHORT).show()
            }
        })
    }
}