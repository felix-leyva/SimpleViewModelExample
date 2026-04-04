package de.felixlf.simpleviewmodelexample.domain

data class Genre(val id: String, val name: String)

data class Artist(val id: String, val name: String, val genreId: String)

data class Album(val id: String, val title: String, val artistId: String, val year: Int)

data class Track(val id: String, val title: String, val durationSeconds: Int)
