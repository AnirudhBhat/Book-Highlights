package com.abhat.bookhighlights.bookslist

import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import org.jsoup.Jsoup
import java.io.File

class BooksParser {
    val bookList = mutableListOf<Book>()

    fun parseHtml(
        htmlFiles: List<File>,
    ): List<Book> {
        bookList.clear()
        htmlFiles.forEach { file ->
            val document = Jsoup.parse(file, "UTF-8")
            val noteHeading = document.getElementsByClass("noteHeading").eachText()
            val noteText = document.getElementsByClass("noteText").eachText()
            val highlights = mutableListOf<Highlight>()
            if (noteHeading.size == noteText.size) {
                noteText.forEachIndexed { index,  text ->
                    highlights.add(
                        Highlight(
                            line = "",
                            heading = noteHeading[index].replace("Highlight (yellow) -", "").replace(">", "-"),
                            highlightText = noteText[index]
                        ))
                }
            } else {
                noteText.forEachIndexed { index,  text ->
                    highlights.add(
                        Highlight(
                            line = "",
                            heading = "",
                            highlightText = noteText[index]
                        )
                    )
                }
            }
            bookList.add(
                Book(
                    title = file.name.replace("- Notebook.html", ""),
                    highlights = highlights
                )
            )
        }
        return bookList
    }
}