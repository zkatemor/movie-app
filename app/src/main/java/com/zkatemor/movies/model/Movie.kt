package com.zkatemor.movies.model

import java.io.Serializable

class Movie : @Transient Serializable {
    private var id: Int = 0
    private var name: String = ""
    private var description: String = ""
    private var date: String = ""
    private var imageURL: String = ""
    private var isFavorites: Boolean = false

    constructor(
        id: Int,
        title: String,
        description: String,
        date: String,
        imageURL: String,
        isFavorites: Boolean
    ) {
        this.id = id
        this.name = title
        this.description = description
        this.date = date
        this.imageURL = imageURL
        this.isFavorites = isFavorites
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

    val getIsFavorites: Boolean
        get() = isFavorites
}