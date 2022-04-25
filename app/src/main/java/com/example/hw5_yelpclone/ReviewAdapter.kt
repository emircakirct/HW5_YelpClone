package com.example.hw5_yelpclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class ReviewAdapter(val context: Context, val restaurants: List<Restaurant>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant)
    }

    override fun getItemCount() = restaurants.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val restaurantName = itemView.findViewById<TextView>(R.id.restaurantName)
        private val restaurantReview = itemView.findViewById<TextView>(R.id.restaurantReview)
        private val restaurantAddress = itemView.findViewById<TextView>(R.id.restaurantAddress)
        private val restaurantCategory = itemView.findViewById<TextView>(R.id.restaurantCategory)
        private val restaurantPrice = itemView.findViewById<TextView>(R.id.restaurantPrice)
        private val restaurantDistance = itemView.findViewById<TextView>(R.id.restaurantDistance)
        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)

        fun bind(restaurant: Restaurant) {
            restaurantName.text = restaurant.name
            ratingBar.rating = restaurant.rating.toFloat()
            restaurantReview.text = "${restaurant.review_count} Reviews"
            restaurantAddress.text = restaurant.location.address1
            restaurantCategory.text = restaurant.categories[0].title
            restaurantPrice.text = restaurant.price
            restaurantDistance.text = restaurant.displayDistance()
            Glide.with(context).load(restaurant.image_url).apply(
                RequestOptions().transforms(
                CenterCrop(), RoundedCorners(20)
            )).error(R.drawable.ic_launcher_background).into(imageView)

        }
    }

}
