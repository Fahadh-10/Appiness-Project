package com.example.appinessproject.adapter

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.appiness.model.Product
import com.example.appinessproject.managers.Utils
import com.example.appinessproject.databinding.MainListBinding
import com.squareup.picasso.Picasso

class MainListADTR() : RecyclerView.Adapter<MainListADTR.HomeListVH>(){

    var productLists = ArrayList<Product>()

    private var mOnItemClickListeners: OnItemClickListeners? = null

    interface OnItemClickListeners {
        fun onItemClick(position: Int, product: Product)
    }

    fun setOnClickListeners(onItemClick: OnItemClickListeners) {
        this.mOnItemClickListeners = onItemClick
    }

    class HomeListVH(mBinding: MainListBinding): RecyclerView.ViewHolder(mBinding.root){
        val binding: MainListBinding = mBinding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListVH {
        return HomeListVH(
            MainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return productLists.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HomeListVH, position: Int) {
        val productList = productLists[position]
        holder.binding.titleTV.text = productList.name
        holder.binding.descriptionTV.text = productList.description
        holder.binding.sellingPriceTV.text = Utils.toCurrencyString(productList.price?.toDouble())
        holder.binding.sellingPriceTV.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.discountPriceTV.text = Utils.toCurrencyString(productList.discount?.toDouble()
            ?.let { (productList.price?.toDouble())?.minus(it) })
        holder.binding.discountPercentTV.text = productList.let {
            Utils.calculateDiscountPercentage(it)
        }

        if (productList.imagePath?.square?.isNotEmpty() == true){
            Picasso.get()
                .load(productList.imagePath?.square?:"")
                .resize(800, 600)
                .centerInside()
                .into(holder.binding.articleIV)
        }

        holder.binding.parentCL.setOnClickListener {
            if (productList != null) {
                mOnItemClickListeners?.onItemClick(position, productList)
            }
        }
    }

}