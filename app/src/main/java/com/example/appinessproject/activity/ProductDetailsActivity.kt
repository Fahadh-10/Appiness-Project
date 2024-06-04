package com.example.appinessproject.activity

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import com.example.appiness.model.Language
import com.example.appiness.model.RealmProduct
import com.example.appinessproject.Dao.HomeListDao
import com.example.appinessproject.R
import com.example.appinessproject.managers.Utils
import com.example.appinessproject.databinding.ActivityProductDetailsBinding
import com.example.appinessproject.helpers.NavKey
import com.squareup.picasso.Picasso
import io.realm.RealmList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        readBundles()
        setUpListeners()
    }

    /**
     * This function is used to read the bundles
     */
    private fun readBundles() {
        if (intent.hasExtra(NavKey.PRODUCT_ID)) {
            val productId = intent.getStringExtra(NavKey.PRODUCT_ID)
            CoroutineScope(Dispatchers.Main).launch {
                val product = productId?.let { HomeListDao.getProductById(it) }
                if (product != null) {
                    setUpViews(product)
                }
            }
        }
    }

    /**
     * Sets up the views with data from the given [product].
     */
    private fun setUpViews(product: RealmProduct?) {
        binding.productNameTV.text = product?.name
        binding.descriptionTV.text = product?.description
        binding.sellingPriceTV.text = Utils.toCurrencyString(product?.price?.toDouble())
        binding.sellingPriceTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        binding.discountPriceTV.text = Utils.toCurrencyString(product?.discount?.toDouble()
            ?.let { (product.price?.toDouble())?.minus(it) })
        binding.discountPercentTV.text = calculateDiscountPercentage(product)
        if (!product?.soldCount.isNullOrEmpty()){
            binding.soldOutCopiesTV.visibility = VISIBLE
            binding.soldOutCopiesTV.text = buildString {
                append(getString(R.string.sold_out_copies))
                append(product?.soldCount)
            }
        }
        binding.pageNoTV.text = buildString {
            append(getString(R.string.total_page))
            append(product?.pages)
        }
        if ((product?.avg ?: 0.0) > 0.0){
            binding.productRatingStarRB.visibility = VISIBLE
            binding.productRatingStarRB.rating = product?.avg?.toFloat()?: 0.0f
        }
        binding.availableLanguagesTV.text = getAvailableLanguagesText(product?.availableLanguages)
        binding.reportTypeTV.text = buildString {
            append(getString(R.string.report_type))
            append(product?.reportType)
        }
        binding.authenticTV.text = buildString {
            append(getString(R.string.authentic))
            append(product?.authentic)
        }
        binding.remediesTV.text = buildString {
            append(getString(R.string.remedies))
            append(product?.remedies)
        }
        binding.vedicTV.text = buildString {
            append(getString(R.string.vedic))
            append(product?.vedic)
        }

        if (product?.imagePath?.wide?.isNotEmpty() == true){
            Picasso.get()
                .load(product.imagePath?.wide)
                .resize(800, 600)
                .centerInside()
                .into(binding.productIV)
        }
    }

    /**
     * Sets up listeners for UI elements.
     */
    private fun setUpListeners(){
        binding.backIV.setOnClickListener {
            finish()
        }
    }

    /**
     * Generates the text representation of available languages from the provided [languages].
     * If the list of languages is empty or null, returns "Languages not available".
     * @param languages The list of available languages.
     * @return The text representation of available languages.
     */
    private fun getAvailableLanguagesText(languages: RealmList<Language>?): String {
        val stringBuilder = StringBuilder()
        if (!languages.isNullOrEmpty()) {
            for ((index, language) in languages.withIndex()) {
                stringBuilder.append(language?.language)
                if (index < languages.size - 1) {
                    stringBuilder.append(", ")
                }
            }
        } else {
            return "Languages not available"
        }
        return stringBuilder.toString()
    }

    /**
     * Calculates the discount percentage for a given product.
     * @return The discount percentage as a string.
     */
    private fun calculateDiscountPercentage(product: RealmProduct?): String {
        val realPrice = product?.price?.toDouble()
        val discountAmount = product?.discount?.toDouble()
        val oldPrice = realPrice?.plus(discountAmount?: 0.0)
        val discountPercentage = (discountAmount?.div(oldPrice?: 0.0))?.times(100)
        val roundedDiscountPercentage = if ((discountPercentage ?: 0.0) > 0) discountPercentage?.roundToInt() else 0
        return roundedDiscountPercentage.toString().plus("%")
    }
}