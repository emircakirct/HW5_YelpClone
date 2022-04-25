package com.example.hw5_yelpclone

data class RestaurantResponse( val businesses: List<Restaurant>)

data class Restaurant( val rating: Double,
                  val price: String,
                  val name: String,
                  val review_count:Long,
                  val image_url: String,
                  val distance: Double,
                  val categories: List<Category>,
                  val location: StoreLocation) {
    
    fun displayDistance(): String {
        val milesPerMeter = 0.000621371
        val distanceInMiles = "%.2f".format(distance * milesPerMeter)
        return "$distanceInMiles mi"
    }
}

data class StoreLocation(  val address1: String)

data class Category(  val title: String,)
