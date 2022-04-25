package com.example.hw5_yelpclone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private const val TAG = "MainActivity"
private const val BASE_URL = "https://api.yelp.com/v3/"
private const val API_KEY ="3Sp90UYlNSS1kxs9i9Qg2ttj8tlb1LMeMjEc5JBXUBkzfRkL-YuxE0cvCZHp2i9EjvvROvlIOG1oinV5CWVIfTeLWInUR4Zybl9tKTxFP8PFJlBD6kUc0IG-l4JjYnYx"
class MainActivity : AppCompatActivity() {
    private lateinit var term: AppCompatEditText
    private lateinit var location: AppCompatEditText
    private lateinit var recycleView: RecyclerView
    private lateinit var adapter: ReviewAdapter
    private val restaurants = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        term = findViewById(R.id.searchTerm)
        location = findViewById(R.id.searchLocation)

        recycleView = findViewById(R.id.listRestaurants)
        adapter = ReviewAdapter(this, restaurants)
        recycleView.adapter = adapter

        val searchButton = findViewById<AppCompatButton>(R.id.btnSearch)

        searchButton.setOnClickListener {
            if (searchValidated()) {
                CallAPI(term = term.text.toString(), location = location.text.toString())
            }
            (searchTerm.hideKeyboard())
            (searchLocation.hideKeyboard())
        }
    }

    private fun searchValidated(): Boolean {
        if (term.text.isNullOrEmpty()) {
            //show dialog
            showDialog("Search term missing","Search term cannot be empty. please enter the term")
            return false
        } else if (location.text.isNullOrEmpty()) {
            showDialog("Location is missing","Location cannot be empty. please enter the location")
            return false

        }

        return true
    }

    private  fun showDialog(titleText: String, descriptionText: String) {
       val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val view = layoutInflater.inflate(R.layout.layout_dialog,null)
        builder.setView(view)
        val dialog = builder.create()
        val title = view.findViewById<TextView>(R.id.txtTitle)
        val description = view.findViewById<TextView>(R.id.txtDescription)
        title.text = titleText
        description.text = descriptionText
         view.findViewById<AppCompatButton>(R.id.btnOk).setOnClickListener {
             dialog.dismiss()
         }

        dialog.show()
    }


    fun View.hideKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun CallAPI(term: String, location: String) {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.searchRestaurants("Bearer $API_KEY", term, "New Britain").enqueue(object :
            Callback<RestaurantResponse> {
            override fun onResponse(call: Call<RestaurantResponse>, restaurantResponse: Response<RestaurantResponse>) {
                Log.i(TAG, "onResponse: $restaurantResponse")
                val body = restaurantResponse.body()
                if (body == null) {
                    Log.w(TAG, "No valid response from API")
                    return
                }
                restaurants.addAll(body.businesses)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                Log.i(TAG, "onFailure: $t")
            }

        })
    }
}