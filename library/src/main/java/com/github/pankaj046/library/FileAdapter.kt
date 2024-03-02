package com.github.pankaj046.library

import android.R.attr.path
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class FileAdapter : ListAdapter<String, FileAdapter.FileViewHolder>(FileItemDiffCallback()) {

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

            Log.e("AAAAAAAA", "bind: ${file.exists()}")

            val thumbnail = if (isVideoFile(file.absolutePath)) {
                ThumbnailUtils.createVideoThumbnail(file.absolutePath, MediaStore.Images.Thumbnails.MINI_KIND)
            } else {
                BitmapFactory.decodeFile(file.absolutePath)
            }
            thumbnail?.let {
                imageView.setImageBitmap(thumbnail)
            }

            if (isVideoFile(file.absolutePath)) {
                playButton.visibility = View.VISIBLE
            } else {
                playButton.visibility = View.GONE
            }

            binding.setOnClickListener {
                val i = Intent()
                i.setAction(Intent.ACTION_VIEW)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.setDataAndType(Uri.parse("file://" + file.absolutePath),
                    "application/vnd.android.package-archive"
                )
                it.context.startActivity(i)
            }
        }

        private fun isVideoFile(filePath: String): Boolean {
            val extension = filePath.substringAfterLast('.')
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
            return mimeType?.startsWith("video/") == true
        }
    }
}



