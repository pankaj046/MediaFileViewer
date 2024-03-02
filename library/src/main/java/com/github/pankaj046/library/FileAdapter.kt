package com.github.pankaj046.library

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class FileAdapter : ListAdapter<String, FileAdapter.FileViewHolder>(FileItemDiffCallback()) {

    private val executor: Executor = Executors.newFixedThreadPool(4)
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_file, parent, false)
        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FileViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {

        fun bind(filePath: String) {
            val imageView = binding.findViewById<ImageView>(R.id.imageView)
            val playButton = binding.findViewById<ImageView>(R.id.playButton)
            val file = File(filePath)
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
            }
        }

        private fun isVideoFile(filePath: String): Boolean {
            val extension = filePath.substringAfterLast('.')
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            return mimeType?.startsWith("video/") == true
        }
    }
}



