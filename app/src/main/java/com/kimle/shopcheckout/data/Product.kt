package com.kimle.shopcheckout.data

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Long
)

object ProductCatalog {
    val products = listOf(
        Product(
            id = "p1",
            name = "Wireless Earbuds",
            description = "Noise-cancelling, 24h battery life",
            priceCents = 4999
        ),
        Product(
            id = "p2",
            name = "Mechanical Keyboard",
            description = "Hot-swappable switches, RGB backlight",
            priceCents = 8999
        ),
        Product(
            id = "p3",
            name = "Portable Charger",
            description = "20,000 mAh, USB-C fast charging",
            priceCents = 2999
        ),
        Product(
            id = "p4",
            name = "Laptop Stand",
            description = "Adjustable aluminum stand, foldable",
            priceCents = 3499
        ),
        Product(
            id = "p5",
            name = "Webcam 1080p",
            description = "Autofocus, built-in privacy shutter",
            priceCents = 5499
        )
    )
}
