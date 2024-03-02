package com.github.pankaj046.library

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.ThumbnailUtils
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class FileAdapter : ListAdapter<String, FileAdapter.FileViewHolder>(FileItemDiffCallback()) {

    private val executor: Executor = Executors.newFixedThreadPool(4)
    private val handler = Handler(Looper.getMainLooper())
    private var fileClickListener: FileClickListener?=null
    private var adapterClickListener: AdapterClickListener?=null
    private var selectedFile: HashSet<String> = hashSetOf()

    fun addListener(fileClickListener: FileClickListener?) {
        this.fileClickListener = fileClickListener
    }

    fun adapterListener(adapterClickListener: AdapterClickListener?) {
        this.adapterClickListener = adapterClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_file, parent, false)
        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position), holder.adapterPosition)
    }

    inner class FileViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        fun bind(filePath: String, adapterPosition: Int) {
            val imageView = binding.findViewById<ImageView>(R.id.imageView)
            val playButton = binding.findViewById<ImageView>(R.id.playButton)
            val isSelected = binding.findViewById<CheckBox>(R.id.isSelected)


            val file = File(filePath)
            isSelected.isChecked = selectedFile.contains(file.absolutePath)
            if (selectedFile.size > 0) {
                isSelected.visibility = View.VISIBLE
            }else{
                isSelected.visibility = View.GONE
            }
            executor.execute {
                val thumbnail = if (isVideoFile(file.absolutePath)) {
                    ThumbnailUtils.createVideoThumbnail(file.absolutePath, MediaStore.Images.Thumbnails.MINI_KIND)
                } else {
                    BitmapFactory.decodeFile(file.absolutePath)
                }
                thumbnail?.let {
                    handler.post {
                        imageView.setImageBitmap(thumbnail)
                    }
                }
            }

            if (isVideoFile(file.absolutePath)) {
                playButton.visibility = View.VISIBLE
            } else {
                playButton.visibility = View.GONE
            }

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
                        isSelected.visibility = View.GONE
                        adapterClickListener?.onClick(selectedFile)
                        notifyDataSetChanged()
                    }
                    notifyItemChanged(adapterPosition)
                }
            }

            binding.setOnLongClickListener {
                if (selectedFile.size == 0) {
                    selectedFile.add(file.absolutePath)
                    adapterClickListener?.onClick(selectedFile)
                    notifyDataSetChanged()
                }
                return@setOnLongClickListener false
            }
        }

        private fun isVideoFile(filePath: String): Boolean {
            val extension = filePath.substringAfterLast('.')
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            return mimeType?.startsWith("video/") == true
        }
    }
}



