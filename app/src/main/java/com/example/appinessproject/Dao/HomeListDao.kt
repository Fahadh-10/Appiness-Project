package com.example.appinessproject.Dao

import com.example.appiness.model.CartItemResponse
import com.example.appiness.model.ImagePath
import com.example.appiness.model.Language
import com.example.appiness.model.RealmProduct
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HomeListDao {

    /**
     * Saves the products from the [cartItemResponse] to the Realm database.
     */
    suspend fun saveProductsToRealm(cartItemResponse: CartItemResponse) {
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransactionAwait { transactionRealm ->
                for ((_, product) in cartItemResponse.products) {
                    val realmProduct = RealmProduct(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        pages = product.pages,
                        pagesInText = product.pagesInText,
                        reportType = product.reportType,
                        authentic = product.authentic,
                        remedies = product.remedies,
                        vedic = product.vedic,
                        price = product.price,
                        discount = product.discount,
                        appDiscount = product.appDiscount,
                        couponDiscount = product.couponDiscount,
                        soldCount = product.soldCount,
                        avg = product.avg
                    )
                    val languages = RealmList<Language>()
                    product.availableLanguages?.forEach { language ->
                        val realmLanguage = Language(language)
                        languages.add(realmLanguage)
                    }
                    realmProduct.availableLanguages = languages

                    val imagePath = ImagePath(product.imagePath?.square, product.imagePath?.wide)
                    realmProduct.imagePath = imagePath

                    transactionRealm.insertOrUpdate(realmProduct)
                }
            }
            realm.close()
        }
    }

    /**
     * Retrieves a product from the Realm database based on its [id].
     * @return The retrieved product from the Realm database, or null if not found.
     */
    suspend fun getProductById(id: String): RealmProduct? {
        return withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            val product = realm.where(RealmProduct::class.java)
                .equalTo("id", id)
                .findFirst()
            val result = product?.let { realm.copyFromRealm(it) }
            realm.close()
            result
        }
    }
}