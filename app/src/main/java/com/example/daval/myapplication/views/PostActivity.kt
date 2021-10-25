package com.example.daval.myapplication.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.example.daval.myapplication.Adapter.RecyclerAdapeter
import com.example.daval.myapplication.Adapter.RecyclerAdapterPost
import com.example.daval.myapplication.R
import com.example.daval.myapplication.databinding.ActivityMainBinding
import com.example.daval.myapplication.databinding.ActivityPostBinding
import com.example.daval.myapplication.models.Post
import com.example.daval.myapplication.models.User
import com.example.daval.myapplication.rest.EndPoints
import com.example.daval.myapplication.rest.MySingleton
import org.json.JSONArray
import org.json.JSONException

class PostActivity : AppCompatActivity() {
    var userRepo : EndPoints = EndPoints()
    var arrayPosts : ArrayList<Post> = ArrayList()
    var arrayUserPost : ArrayList<User> = ArrayList()
    private lateinit var adapter : RecyclerAdapterPost
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding : ActivityPostBinding



    private var idUserPost:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)
        idUserPost = intent.getIntExtra("userId", 0)
        recyclerView = binding.recyclerViewPostsResults
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        adapter  = RecyclerAdapterPost(this, arrayPosts)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        getData()
        getUserPost()
    }

    private fun getUserPost() {
        val queue = MySingleton.getInstance(this).requestQueue
        Log.d("json", "onCreate: " + arrayUserPost.size)
        val jsonArrayRequest= JsonArrayRequest(0,
            (userRepo.URL_BASE+userRepo.GET_USERS),
            null,
            { response: JSONArray ->
                for (i in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(i)
                        if(jsonObject.getInt("id") == idUserPost) {
                        arrayUserPost.add(User(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("phone"),
                            jsonObject.getString("email")
                        ))}

                        Log.d("json", "onCreate: " + jsonObject.getString("phone"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                adapter.notifyDataSetChanged()
                setUserPost(arrayUserPost)


            },
            { error: VolleyError? ->
                Log.d(
                    "volleyResponse",
                    "onCreate: " + "failed"
                )
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun setUserPost(arrayUserPost: ArrayList<User>) {
        binding.namePost.text = arrayUserPost[0].name
        binding.phonePost.text = arrayUserPost[0].phone
        binding.emailPost.text = arrayUserPost[0].email
    }

    private fun getData() {
        val queue = MySingleton.getInstance(this).requestQueue
        val jsonArrayRequest= JsonArrayRequest(0,
            (userRepo.URL_BASE+userRepo.GET_POST_USER),
            null,
            { response: JSONArray ->
                for (i in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(i)
                        if (jsonObject.getInt("userId") ==idUserPost){
                            arrayPosts.add(Post(
                                jsonObject.getString("title"),
                                jsonObject.getString("body"),
                            ))
                        }
                        Log.d("json", "onCreate: " + arrayPosts.size)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                adapter.notifyDataSetChanged()


            },
            { error: VolleyError? ->
                Log.d(
                    "volleyResponse",
                    "onCreate: " + "failed"
                )
            }
        )
        queue.add(jsonArrayRequest)
    }
}


