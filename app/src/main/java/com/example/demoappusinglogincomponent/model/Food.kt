package com.example.demoappusinglogincomponent.model

import java.io.Serializable

class Food : Serializable {
    private var id = 0
    private var name: String? = null
    private var image: String? = null
    private var banner: String? = null
    private var description: String? = null
    private var price = 0
    private var sale = 0
    private var count = 0
    private var totalPrice = 0
    private var popular = false
    private var selected: Boolean = false

    private var images: MutableList<Image>? = null

    fun getId(): Int = id
    fun setId(id: Int) { this.id = id }

    fun getName(): String? = name
    fun setName(name: String?) { this.name = name }

    fun getPrice(): Int = price
    fun setPrice(price: Int) { this.price = price }

    fun getRealPrice(): Int {
        return if (sale <= 0) price else price - price * sale / 100
    }

    fun getImage(): String? = image
    fun setImage(image: String?) { this.image = image }

    fun getBanner(): String? = banner
    fun setBanner(banner: String?) { this.banner = banner }

    fun getDescription(): String? = description
    fun setDescription(description: String?) { this.description = description }

    fun getSale(): Int = sale
    fun setSale(sale: Int) { this.sale = sale }

    fun getCount(): Int = count
    fun setCount(count: Int) { this.count = count }

    fun getTotalPrice(): Int = totalPrice
    fun setTotalPrice(totalPrice: Int) { this.totalPrice = totalPrice }

    fun isPopular(): Boolean = popular
    fun setPopular(popular: Boolean) { this.popular = popular }

    fun getImages(): MutableList<Image>? = images
    fun setImages(images: MutableList<Image>?) { this.images = images }

    fun isSelected(): Boolean = selected
    fun setSelected(value: Boolean) { selected = value }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Food) return false
        return id == other.id &&
                name == other.name &&
                image == other.image &&
                price == other.price &&
                sale == other.sale
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + price
        result = 31 * result + sale
        return result
    }
}
