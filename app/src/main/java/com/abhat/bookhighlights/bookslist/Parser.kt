package com.abhat.bookhighlights.bookslist

import com.abhat.bookhighlights.bookslist.model.HtmlDocument
import java.io.File

interface Parser {
    fun parseHtml(htmlFiles: List<File>): MutableList<HtmlDocument>
}