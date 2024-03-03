#Simple Media file selector

[![](https://jitpack.io/v/pankaj046/MediaFileViewer.svg)](https://jitpack.io/#pankaj046/MediaFileViewer)

<img src='https://raw.githubusercontent.com/pankaj046/MediaFileViewer/master/demo/demo.gif' width=180 height=350/>

Step 1. Add the JitPack repository to your build file

`repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}`

Step 2. Add the dependency

`implementation 'com.github.pankaj046:MediaFileViewer:0.0.1'`

Step 3. Check and allow file permission 
check this URL : https://developer.android.com/training/data-storage/shared/media

Step 4. Add this code in UI

    <com.github.pankaj046.library.FileExplorer
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/file_explorer"
        app:backgroundColor="@color/black"
        app:barColor="@color/black"
        app:selectedTextColor="@color/white"
        app:buttonColor="@color/white"
        app:buttonTextColor="@color/black"
        app:selectorColor="@color/white"/>

Step 5. Listener single file and multi file selector method


        val fileExplorer = findViewById(R.id.file_explorer)
        fileExplorer?.setListener(object : FileClickListener {
            override fun onClick(file: File?) {
                Toast.makeText(this@MainActivity, file?.name, Toast.LENGTH_SHORT).show()
            }

            override fun mutipleSelected(paths: HashSet<String>?) {
                Toast.makeText(this@MainActivity, paths?.size.toString(), Toast.LENGTH_SHORT).show()
            }
        })

