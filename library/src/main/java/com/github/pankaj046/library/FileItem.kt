package com.github.pankaj046.library

import android.net.Uri

data class FileItem (
    var name: String?=null,
    var uri: Uri?=null,
    var isSelected:Boolean?=null
)