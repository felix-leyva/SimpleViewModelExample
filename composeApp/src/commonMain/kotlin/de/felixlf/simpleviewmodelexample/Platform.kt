package de.felixlf.simpleviewmodelexample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform