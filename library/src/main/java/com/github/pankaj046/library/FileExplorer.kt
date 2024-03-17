package com.github.pankaj046.library

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FileExplorer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val multiSelectionView: RelativeLayout
    private val selectedItemCount: TextView
    private val btnSelectedItem: Button
    private val fileAdapter = FileAdapter()
    private var selectedFile: HashSet<String> = hashSetOf()
    private var fileClickListener: FileClickListener?=null

    private val DEFAULT_BACKGROUND_COLOR = Color.DKGRAY
    private val DEFAULT_BUTTON_COLOR = Color.WHITE
    private val DEFAULT_BUTTON_TEXT_COLOR = Color.BLACK
    private val DEFAULT_BAR_COLOR = Color.BLACK
    private val DEFAULT_SELECTED_TEXT_COLOR = Color.WHITE
    private val DEFAULT_FILE_TYPE = 0

    init {
        inflate(context, R.layout.layout_file_explorer, this)
        recyclerView = findViewById(R.id.recyclerView)
        multiSelectionView = findViewById(R.id.bottomLayout)
        selectedItemCount = findViewById(R.id.selectedItemCount)
        btnSelectedItem = findViewById(R.id.btnSelectedItem)
        val type = attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.media)
            val bgColor = typedArray.getColor(R.styleable.media_backgroundColor, DEFAULT_BACKGROUND_COLOR)
            val buttonColor = typedArray.getColor(R.styleable.media_buttonColor, DEFAULT_BUTTON_COLOR)
            val buttonTextColor = typedArray.getColor(R.styleable.media_buttonTextColor, DEFAULT_BUTTON_TEXT_COLOR)
            val barColor = typedArray.getColor(R.styleable.media_barColor, DEFAULT_BAR_COLOR)
            val selectedTextColor = typedArray.getColor(R.styleable.media_textColor, DEFAULT_SELECTED_TEXT_COLOR)
            val mediaType = typedArray.getInt(R.styleable.media_fileType, DEFAULT_FILE_TYPE)
            this.setBackgroundColor(bgColor)
            multiSelectionView.setBackgroundColor(barColor)
            selectedItemCount.setTextColor(selectedTextColor)
            btnSelectedItem.setBackgroundColor(buttonColor)
            btnSelectedItem.setTextColor(buttonTextColor)
            fileAdapter.setCheckBoxColor(typedArray)
            mediaType
        }
        setupRecyclerView(type!!)
    }

    private fun setupRecyclerView(mediaType: Int = 0) {
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        val spanCount = 3
        val spacing = 10
        val includeEdge = false
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        val data = getMediaFilePaths(context, mediaType)
        recyclerView.setItemViewCacheSize(data.size)
        fileAdapter.submitList(data)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = fileAdapter
        fileAdapter.adapterListener(object : AdapterClickListener {
            override fun onClick(files: HashSet<String>?) {
                files?.let {
                    val paddingBottomPx = if (it.size>0){
                        selectedFile = files
                        multiSelectionView.visibility = View.VISIBLE
                        recyclerView.clipToPadding = false
                        selectedItemCount.text = String.format("%d selected", it.size)
                        resources.getDimensionPixelSize(R.dimen.bottom_padding)
                    }else{
                        multiSelectionView.visibility = View.GONE
                        recyclerView.clipToPadding = true
                        0
                    }
                    recyclerView.setPadding(0, 0, 0, paddingBottomPx)
                }
            }
        })

        btnSelectedItem.setOnClickListener { fileClickListener?.multiSelection(selectedFile) }
    }


    fun setListener(fileClickListener: FileClickListener) {
        this.fileClickListener = fileClickListener
        fileAdapter.addListener(fileClickListener)
    }

    private fun getMediaFilePaths(context: Context, mediaType: Int): ArrayList<String> {
        val filePaths = ArrayList<String>()
        val contentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        val selection = when (mediaType) {
            1 -> {
                "${MediaStore.MediaColumns.MIME_TYPE} LIKE 'image/%'"
            }
            2 -> {
                "${MediaStore.MediaColumns.MIME_TYPE} LIKE 'video/%'"
            }
            else -> {
                "${MediaStore.MediaColumns.MIME_TYPE} LIKE 'image/%' OR ${MediaStore.MediaColumns.MIME_TYPE} LIKE 'video/%'"
            }
        }


        val sortOrder = "${MediaStore.MediaColumns.DATE_ADDED} DESC"
        val cursor = contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            null,
            sortOrder
        )
        cursor?.use { dataCursor ->
            val dataColumnIndex = dataCursor.getColumnIndex(MediaStore.MediaColumns.DATA)
            while (dataCursor.moveToNext()) {
                val filePath = dataCursor.getString(dataColumnIndex)
                filePaths.add(filePath)
            }
        }
        return filePaths
    }


    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) :
        RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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