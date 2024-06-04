package com.example.appinessproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.appinessproject.adapter.MainListADTR
import com.example.appiness.model.CartItemResponse
import com.example.appiness.model.Product
import com.example.appinessproject.Dao.HomeListDao
import com.example.appinessproject.databinding.ActivityMainBinding
import com.example.appinessproject.helpers.GET_PRODUCT_LIST
import com.example.appinessproject.helpers.NavKey
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainListADTR: MainListADTR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
        setAdapter()
        fetchProducts()
    }

    /**
     * Sets up the RecyclerView adapter and click listeners.
     */
    private fun setAdapter() {
        binding.homeRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mainListADTR = MainListADTR()
        binding.homeRV.adapter = mainListADTR
        mainListADTR.setOnClickListeners(object : MainListADTR.OnItemClickListeners{
            override fun onItemClick(position: Int, product: Product) {
                val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                intent.putExtra(NavKey.PRODUCT_ID, product.id)
                startActivity(intent)
            }
        })
    }

    /**
     * Fetches products from the server.
     */
    private fun fetchProducts() {
        val request = JsonObjectRequest(
            Request.Method.GET, GET_PRODUCT_LIST, null,
            { response ->
                CoroutineScope(Dispatchers.Main).launch {
                    handleProductFetchSuccess(response)
                }
            },
            { error ->
                handleProductFetchError(error.message ?: "Unknown error")
            })

        Volley.newRequestQueue(this).add(request)
    }

    /**
     * Handles the successful response from the product fetch API call.
     */
    private suspend fun handleProductFetchSuccess(response: JSONObject) {
        val cartItemResponse = Gson().fromJson(response.toString(), CartItemResponse::class.java)
        val productsMap = cartItemResponse.products

        if (productsMap.isNotEmpty()) {
            val productList = productsMap.map { it.value } as ArrayList<Product>
            binding.progressBar.visibility = View.GONE
            binding.homeRV.visibility = View.VISIBLE
            withContext(Dispatchers.IO) {
                HomeListDao.saveProductsToRealm(cartItemResponse)
            }
            mainListADTR.productLists = productList
            mainListADTR.notifyDataSetChanged()
        } else {
            binding.homeRV.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    /**
     * Handles errors that occur during the product fetch API call.
     */
    private fun handleProductFetchError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

}