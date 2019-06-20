package com.zkatemor.movies.model

class Movie {
    private var id: Int = 0
    private var name: String = ""
    private var description: String = ""
    private var date: String = ""
    private var imageURL: String = ""

    constructor(
        id: Int,
        title: String,
        description: String,
        date: String,
        imageURL: String
    ) {
        this.id = id
        this.name = title
        this.description = description
        this.date = date
        this.imageURL = imageURL
    }

    val getId: Int
        get() = id

    val getName: String
        get() = name

    val getDescription: String
        get() = description

    val getDate: String
        get() = date

    val getImageURL: String
        get() = imageURL
}