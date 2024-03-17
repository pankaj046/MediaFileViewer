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
        fileExplorer = findViewById(R.id.file_explorer)
        fileExplorer?.setListener(object : FileClickListener {
            override fun singleSelection(path: String?) {
                Toast.makeText(this@MainActivity, path, Toast.LENGTH_SHORT).show()
            }

            override fun multiSelection(paths: HashSet<String>?) {
                Toast.makeText(this@MainActivity, paths?.size.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}