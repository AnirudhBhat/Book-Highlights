package com.abhat.bookhighlights

import com.abhat.bookhighlights.bookslist.parser.BooksParser
import com.abhat.bookhighlights.bookslist.parser.Parser
import com.abhat.bookhighlights.bookslist.model.Book
import com.abhat.bookhighlights.bookslist.model.Highlight
import com.abhat.bookhighlights.bookslist.model.HtmlDocument
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BooksParserTest {
    private lateinit var htmlParser: Parser
    private lateinit var booksParser: BooksParser

    @Before
    fun setup() {
        htmlParser = mock {
            on {
                parseHtml(any())
            }.thenReturn(
                mutableListOf(
                    HtmlDocument(
                        heading = listOf(
                            "Highlight (yellow) - heading 1 book 1",
                            "Highlight (yellow) - heading 2 book 1",
                            "Highlight (yellow) - heading 3 book 1"
                        ),
                        highlight = listOf(
                            "highlight 1 book 1",
                            "highlight 2 book 1",
                            "highlight 3 book 1"
                        ),
                        bookName = "Book name 1"
                    ),
                    HtmlDocument(
                        heading = listOf(
                            "heading 1 book 2",
                            "heading 2 book 2",
                            "heading 3 book 2"
                        ),
                        highlight = listOf(
                            "highlight 1 book 2",
                            "highlight 2 book 2",
                            "highlight 3 book 2"
                        ),
                        bookName = "Book name 2"
                    ),
                    HtmlDocument(
                        heading = listOf(
                            "heading 1 book 3",
                            "Highlight (yellow) - heading 2 book 3",
                            "Highlight (yellow) - heading 3 book 3"
                        ),
                        highlight = listOf(
                            "highlight 1 book 3",
                            "highlight 2 book 3",
                            "highlight 3 book 3"
                        ),
                        bookName = "Book name 3"
                    )
                )
            )
        }
        booksParser = BooksParser(htmlParser)
    }

    @Test
    fun `when parse books called, then correct highlist list is returned`() {
        val expectedBooksList = listOf(
            Book(
                title = "Book name 1",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 1 book 1"
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 2 book 1"
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 3 book 1"
                    )
                )
            ),
            Book(
                title = "Book name 2",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 1 book 2"
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 2 book 2"
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 3 book 2"
                    )
                )
            ),
            Book(
                title = "Book name 3",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 1 book 3"
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 2 book 3"
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = "highlight 3 book 3"
                    )
                )
            )
        )

        Assert.assertEquals(
            expectedBooksList[0].highlights[0].highlightText,
            booksParser.parseBooks(mock())[0].highlights[0].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[0].highlights[1].highlightText,
            booksParser.parseBooks(mock())[0].highlights[1].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[0].highlights[2].highlightText,
            booksParser.parseBooks(mock())[0].highlights[2].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[1].highlights[0].highlightText,
            booksParser.parseBooks(mock())[1].highlights[0].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[1].highlights[1].highlightText,
            booksParser.parseBooks(mock())[1].highlights[1].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[1].highlights[2].highlightText,
            booksParser.parseBooks(mock())[1].highlights[2].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[2].highlights[0].highlightText,
            booksParser.parseBooks(mock())[2].highlights[0].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[2].highlights[1].highlightText,
            booksParser.parseBooks(mock())[2].highlights[1].highlightText
        )
        Assert.assertEquals(
            expectedBooksList[2].highlights[2].highlightText,
            booksParser.parseBooks(mock())[2].highlights[2].highlightText
        )
    }

    @Test
    fun `when parse books called, then correct heading list is returned`() {
        val expectedBooksList = listOf(
            Book(
                title = "Book name 1",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "heading 1 book 1",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "heading 2 book 1",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "heading 3 book 1",
                        highlightText = ""
                    )
                )
            ),
            Book(
                title = "Book name 2",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "heading 1 book 2",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "heading 2 book 2",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "heading 3 book 2",
                        highlightText = ""
                    )
                )
            ),
            Book(
                title = "Book name 3",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "heading 1 book 3",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "heading 2 book 3",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "heading 3 book 3",
                        highlightText = ""
                    )
                )
            )
        )

        Assert.assertEquals(
            expectedBooksList[0].highlights[0].heading,
            booksParser.parseBooks(mock())[0].highlights[0].heading
        )
        Assert.assertEquals(
            expectedBooksList[0].highlights[1].heading,
            booksParser.parseBooks(mock())[0].highlights[1].heading
        )
        Assert.assertEquals(
            expectedBooksList[0].highlights[2].heading,
            booksParser.parseBooks(mock())[0].highlights[2].heading
        )
        Assert.assertEquals(
            expectedBooksList[1].highlights[0].heading,
            booksParser.parseBooks(mock())[1].highlights[0].heading
        )
        Assert.assertEquals(
            expectedBooksList[1].highlights[1].heading,
            booksParser.parseBooks(mock())[1].highlights[1].heading
        )
        Assert.assertEquals(
            expectedBooksList[1].highlights[2].heading,
            booksParser.parseBooks(mock())[1].highlights[2].heading
        )
        Assert.assertEquals(
            expectedBooksList[2].highlights[0].heading,
            booksParser.parseBooks(mock())[2].highlights[0].heading
        )
        Assert.assertEquals(
            expectedBooksList[2].highlights[1].heading,
            booksParser.parseBooks(mock())[2].highlights[1].heading
        )
        Assert.assertEquals(
            expectedBooksList[2].highlights[2].heading,
            booksParser.parseBooks(mock())[2].highlights[2].heading
        )
    }

    @Test
    fun `when parse books called, then correct book name is returned`() {
        val expectedBooksList = listOf(
            Book(
                title = "Book name 1",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    )
                )
            ),
            Book(
                title = "Book name 2",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    )
                )
            ),
            Book(
                title = "Book name 3",
                thumbnail = "",
                highlights = mutableListOf(
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    ),
                    Highlight(
                        line = "",
                        heading = "",
                        highlightText = ""
                    )
                )
            )
        )

        Assert.assertEquals(
            expectedBooksList[0].title,
            booksParser.parseBooks(mock())[0].title
        )
        Assert.assertEquals(
            expectedBooksList[0].title,
            booksParser.parseBooks(mock())[0].title
        )
        Assert.assertEquals(
            expectedBooksList[0].title,
            booksParser.parseBooks(mock())[0].title
        )
        Assert.assertEquals(
            expectedBooksList[1].title,
            booksParser.parseBooks(mock())[1].title
        )
        Assert.assertEquals(
            expectedBooksList[1].title,
            booksParser.parseBooks(mock())[1].title
        )
        Assert.assertEquals(
            expectedBooksList[1].title,
            booksParser.parseBooks(mock())[1].title
        )
        Assert.assertEquals(
            expectedBooksList[2].title,
            booksParser.parseBooks(mock())[2].title
        )
        Assert.assertEquals(
            expectedBooksList[2].title,
            booksParser.parseBooks(mock())[2].title
        )
        Assert.assertEquals(
            expectedBooksList[2].title,
            booksParser.parseBooks(mock())[2].title
        )
    }
}