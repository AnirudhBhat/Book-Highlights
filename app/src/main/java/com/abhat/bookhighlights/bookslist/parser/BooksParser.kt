package com.abhat.bookhighlights.bookslist.parser

import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import java.io.File

class BooksParser(
    private val htmlParser: Parser
) {
    private val bookList = mutableListOf<Book>()

    fun parseBooks(
        books: List<File>,
    ): List<Book> {
        bookList.clear()
        val htmlDocumentsList = htmlParser.parseHtml(books)
        htmlDocumentsList.forEachIndexed { index, htmlDocument ->
            val highlights = mutableListOf<Highlight>()
            htmlDocument.highlight.forEachIndexed { index, highlight ->
                highlights.add(
                    Highlight(
                        line = "",
                        heading = htmlDocument.heading[index].replace("Highlight (yellow) - ", "")
                            .replace(">", "-"),
                        highlightText = highlight
                    )
                )
            }
            bookList.add(
                Book(
                    title = htmlDocumentsList[index].bookName,
                    thumbnail = "",
                    highlights = highlights
                )
            )
        }
        return bookList
    }
}