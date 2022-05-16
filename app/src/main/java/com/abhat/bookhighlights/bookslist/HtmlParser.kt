package com.abhat.bookhighlights.bookslist

import com.abhat.bookhighlights.bookslist.model.HtmlDocument
import org.jsoup.Jsoup
import java.io.File

object HtmlParser: Parser {
    override fun parseHtml(htmlFiles: List<File>): MutableList<HtmlDocument> {
        val htmlDocumentsList = mutableListOf<HtmlDocument>()
        htmlFiles.forEach { file ->
            val document = Jsoup.parse(file, "UTF-8")
            val noteHeading = document.getElementsByClass("noteHeading").eachText()
            val noteText = document.getElementsByClass("noteText").eachText()
            htmlDocumentsList.add(
                HtmlDocument(
                    heading = noteHeading,
                    highlight = noteText,
                    bookName = file.name.replace("- Notebook.html", "")
                )
            )
        }
        return htmlDocumentsList
    }
}