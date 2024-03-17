#Simple Media file selector

[![](https://jitpack.io/v/pankaj046/MediaFileViewer.svg)](https://jitpack.io/#pankaj046/MediaFileViewer)

<img src='https://raw.githubusercontent.com/pankaj046/MediaFileViewer/master/demo/demo.gif' width=180 height=350/>

Step 1. Add the JitPack repository to your build file

`repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}`

Step 2. Add the dependency

`implementation 'com.github.pankaj046:MediaFileViewer:0.0.2'`

Step 3. Check and allow file permission to access media files
https://developer.android.com/training/data-storage/shared/media

Step 4. Add this code in UI

    <com.github.pankaj046.library.FileExplorer
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="10dp"
        android:id="@+id/file_explorer"
        app:backgroundColor="@color/black"
        app:barColor="@color/black"
        app:buttonColor="@color/white"
        app:buttonTextColor="@color/black"
        app:textColor="@android:color/white"
        app:fileType="video"
        app:selectorTextColor="@android:color/black"
        app:selectorStrokeColor="@android:color/holo_purple"
        app:selectorBackgroundColor="@android:color/darker_gray"/>

Step 5. Listener single file and multi file selector method


    fileExplorer?.setListener(object : FileClickListener {
            override fun singleSelection(path: String?) {
                Toast.makeText(this@MainActivity, path, Toast.LENGTH_SHORT).show()
            }

            override fun multiSelection(paths: HashSet<String>?) {
                Toast.makeText(this@MainActivity, paths?.size.toString(), Toast.LENGTH_SHORT).show()
            }
        })


TODO
1) Flicker issue when media selection (DONE)
2) when scroll media thumbnail is overlap (DONE)
3) fix lagging issue 
