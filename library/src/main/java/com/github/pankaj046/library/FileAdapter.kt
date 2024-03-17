package com.github.pankaj046.library

import android.content.res.TypedArray
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class FileAdapter : ListAdapter<String, FileAdapter.FileViewHolder>(FileItemDiffCallback()) {

    private val executor: Executor = Executors.newFixedThreadPool(4)
    private var fileClickListener: FileClickListener?=null
    private var adapterClickListener: AdapterClickListener?=null
    private var selectedFile: HashSet<String> = hashSetOf()
    private var selectorColor:TypedArray?=null
    private val DEFAULT_SELECTED_COLOR = Color.WHITE

    fun addListener(fileClickListener: FileClickListener?) {
        this.fileClickListener = fileClickListener
    }

    fun adapterListener(adapterClickListener: AdapterClickListener?) {
        this.adapterClickListener = adapterClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_file, parent, false)
        selectorColor?.let {
           itemView.findViewById<RoundedTextView>(R.id.isSelected).apply {
               setStyleBackground(it)
           }
        }
        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position), holder.adapterPosition)
    }

    fun setCheckBoxColor(checkBoxColor: TypedArray) {
        selectorColor = checkBoxColor
    }

    inner class FileViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        fun bind(filePath: String, adapterPosition: Int) {
            val imageView = binding.findViewById<ImageView>(R.id.imageView)
            val playButton = binding.findViewById<ImageView>(R.id.playButton)
            val isSelected = binding.findViewById<TextView>(R.id.isSelected)

            val file = File(filePath)
            if (selectedFile.size > 0 && selectedFile.contains(file.absolutePath)) {
                isSelected.visibility = View.VISIBLE
            }else{
                isSelected.visibility = View.INVISIBLE
            }

            ImageViewer.Builder(binding.context)
                .setFileUrl(file.absolutePath)
                .load(imageView)
                .setListener(object : ImageViewer.MediaInfoListener {
                    override fun isVideo(isVideo: Boolean) {
                        if (isVideo) {
                            playButton.visibility = View.VISIBLE
                        } else {
                            playButton.visibility = View.INVISIBLE
                        }
                    }
                }).build()


            binding.setOnClickListener {
                if (selectedFile.size == 0){
                    fileClickListener?.onClick(file)
                }

                if (selectedFile.size > 0) {
                    if (selectedFile.contains(file.absolutePath)) {
                        selectedFile.remove(file.absolutePath)
                        adapterClickListener?.onClick(selectedFile)
                    }else{
                        selectedFile.add(file.absolutePath)
                        adapterClickListener?.onClick(selectedFile)
                    }
                    if (selectedFile.size > 0) {
                        isSelected.visibility = View.VISIBLE
                    }else{
                        isSelected.visibility = View.INVISIBLE
                        adapterClickListener?.onClick(selectedFile)
                    }
                    notifyItemChanged(adapterPosition)
                }
            }

            binding.setOnLongClickListener {
                if (selectedFile.size == 0) {
                    selectedFile.add(file.absolutePath)
                    adapterClickListener?.onClick(selectedFile)
                    notifyItemChanged(adapterPosition)
                }
                return@setOnLongClickListener false
            }
        }
    }
}



