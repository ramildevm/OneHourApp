package com.example.onehourapp.helpers


    object UITextHelper{
    fun generateLoremIpsumWords(count: Int): String {
        val words = listOf(
            "Lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
            "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
            "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
        )
        return List(count) { words.random() }.joinToString(" ")
    }
    }
