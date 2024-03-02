package com.github.pankaj046.library

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FileExplorer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private val fileAdapter = FileAdapter()

    init {
        this.layoutManager = GridLayoutManager(context, 3)
        val spanCount = 3
        val spacing = 10
        val includeEdge = false
        addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        val data  = getMediaFilePaths(context)
        fileAdapter.submitList(data)
        fileAdapter.notifyDataSetChanged()
        setHasFixedSize(true)
        adapter = fileAdapter
    }


    @SuppressLint("Range")
    private fun getMediaFilePaths(context: Context): ArrayList<String> {
        val filePaths = ArrayList<String>()
        val contentResolver = context.contentResolver

        val imageQueryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val imageSelection = "${MediaStore.Images.Media.MIME_TYPE} LIKE 'image/%'"
        val imageCursor = contentResolver.query(imageQueryUri, imageProjection, imageSelection, null, null)
        imageCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                val imageName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                filePaths.add(imagePath)
            }
        }

        val videoQueryUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val videoProjection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME
        )
        val videoSelection = "${MediaStore.Video.Media.MIME_TYPE} LIKE 'video/%'"
        val videoCursor = contentResolver.query(videoQueryUri, videoProjection, videoSelection, null, null)
        videoCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                val videoName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                filePaths.add(videoPath)
            }
        }
        return filePaths
    }


    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) :
        ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount
                outRect.right =
                    (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }


}