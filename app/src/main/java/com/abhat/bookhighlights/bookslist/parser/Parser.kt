package com.abhat.bookhighlights.bookslist.parser

import com.abhat.bookhighlights.bookslist.model.HtmlDocument
import java.io.File

interface Parser {
    fun parseHtml(htmlFiles: List<File>): MutableList<HtmlDocument>
}