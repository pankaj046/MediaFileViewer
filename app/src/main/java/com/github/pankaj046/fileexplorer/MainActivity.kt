package com.github.pankaj046.fileexplorer

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.pankaj046.library.FileClickListener
import com.github.pankaj046.library.FileExplorer
import android.Manifest


class MainActivity : AppCompatActivity() {

    private var fileExplorer: FileExplorer?=null
    private val REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkReadExternalStoragePermission()

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

    private fun checkReadExternalStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // Android 14 and above
                val permissions = arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
                if (!permissions.all {
                        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                    }) {
                    requestReadExternalStoragePermission()
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13
                val permissions = arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                )
                if (!permissions.all {
                        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                    }) {
                    requestReadExternalStoragePermission()
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                // Android 10 to 12
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                )
                if (!permissions.all {
                        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                    }) {
                    requestReadExternalStoragePermission()
                }
            }
            else -> {
                // Below Android 10
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestReadExternalStoragePermission()
                }
            }
        }
    }


    private fun requestReadExternalStoragePermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // Android 14 and above
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    ),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                    ),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                // Android 10 to 12
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_MEDIA_LOCATION
                    ),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION
                )
            }
            else -> {
                // Below Android 10
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION
                )
            }
        }
    }

}