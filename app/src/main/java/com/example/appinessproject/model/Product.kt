package com.example.appiness.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.UUID


data class CartItemResponse(
    val products: Map<String, Product>
) : Serializable

data class Product(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("availableLanguages") var availableLanguages: RealmList<String>? = null,
    @SerializedName("pages") var pages: Int? = null,
    @SerializedName("pagesintext") var pagesInText: String? = null,
    @SerializedName("report_type") var reportType: String? = null,
    @SerializedName("authentic") var authentic: String? = null,
    @SerializedName("remedies") var remedies: String? = null,
    @SerializedName("vedic") var vedic: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("discount") var discount: Int? = null,
    @SerializedName("appDiscount") var appDiscount: Int? = null,
    @SerializedName("couponDiscount") var couponDiscount: Int? = null,
    @SerializedName("imagePath") var imagePath: ImagePath? = null,
    @SerializedName("soldcount") var soldCount: String? = null,
    @SerializedName("avg") var avg: Double? = null
) : Serializable

open class RealmProduct(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("availableLanguages") var availableLanguages: RealmList<Language>? = null,
    @SerializedName("pages") var pages: Int? = null,
    @SerializedName("pagesintext") var pagesInText: String? = null,
    @SerializedName("report_type") var reportType: String? = null,
    @SerializedName("authentic") var authentic: String? = null,
    @SerializedName("remedies") var remedies: String? = null,
    @SerializedName("vedic") var vedic: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("discount") var discount: Int? = null,
    @SerializedName("appDiscount") var appDiscount: Int? = null,
    @SerializedName("couponDiscount") var couponDiscount: Int? = null,
    @SerializedName("imagePath") var imagePath: ImagePath? = null,
    @SerializedName("soldcount") var soldCount: String? = null,
    @SerializedName("avg") var avg: Double? = null
) : RealmObject(), Serializable

open class Language(
    var language: String? = null
) : RealmObject()

open class ImagePath(
    @SerializedName("square") var square: String? = null,
    @SerializedName("wide") var wide: String? = null
) : RealmObject(), Serializable

