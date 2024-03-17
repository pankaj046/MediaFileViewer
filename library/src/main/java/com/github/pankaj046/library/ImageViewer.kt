package com.github.pankaj046.library

import android.content.Context
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.ImageView
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ImageViewer private constructor() {
    class Builder(private val context: Context) {

        private val executor: Executor = Executors.newFixedThreadPool(4)
        private val handler = Handler(Looper.getMainLooper())
        private var fileUrl: String? = null
        private var mediaInfoListener: MediaInfoListener? = null

        fun setListener(mediaInfoListener: MediaInfoListener): Builder {
            this.mediaInfoListener = mediaInfoListener
            return this
        }

        fun setFileUrl(fileUrl: String): Builder {
            this.fileUrl = fileUrl
            return this
        }

        fun load(imageView: ImageView): Builder {
            executor.execute {
                if (fileUrl == null) return@execute
                val isVideo = if (isVideoFile()) {
                    handler.post {
                        mediaInfoListener?.isVideo(true)
                    }
                    true
                }else {
                    handler.post {
                        mediaInfoListener?.isVideo(false)
                    }
                    false
                }
                val thumbnail = if (isVideo) {
                    ThumbnailUtils.createVideoThumbnail(fileUrl!!, MediaStore.Images.Thumbnails.MINI_KIND)
                } else {
                    BitmapFactory.decodeFile(fileUrl)
                }
                thumbnail?.let {
                    handler.post {
                        imageView.setImageBitmap(it)
                    }
                }
            }
            return this
        }

        private fun  isVideoFile(): Boolean {
            val extension = fileUrl?.substringAfterLast('.')
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension?.lowercase())
            return mimeType?.startsWith("video/") == true
        }

        fun build(): ImageViewer {
            requireNotNull(fileUrl) { "File URL must be set" }
            return ImageViewer()
        }
    }

    interface MediaInfoListener {
        fun isVideo(isVideo:Boolean)
    }
}