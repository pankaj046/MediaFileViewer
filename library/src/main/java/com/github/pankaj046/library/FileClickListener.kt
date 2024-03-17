package com.github.pankaj046.library

import java.io.File

interface FileClickListener {
    fun singleSelection(path: String?) {}
    fun multiSelection(paths: HashSet<String>?) {}
}