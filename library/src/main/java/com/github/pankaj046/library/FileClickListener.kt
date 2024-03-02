package com.github.pankaj046.library

import java.io.File

interface FileClickListener {
    fun onClick(file: File?)
    fun mutipleSelected(paths: HashSet<String>?)
}