package com.example.daval.myapplication.views

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.example.daval.myapplication.Adapter.RecyclerAdapeter
import com.example.daval.myapplication.BD.DataBaseHelper
import com.example.daval.myapplication.R
import com.example.daval.myapplication.databinding.ActivityMainBinding
import com.example.daval.myapplication.models.User
import com.example.daval.myapplication.rest.EndPoints
import com.example.daval.myapplication.rest.MySingleton
import org.json.JSONArray
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    var userRepo : EndPoints = EndPoints()
    var arrayUsers : ArrayList<User> = ArrayList()
    var arrayUsersFilter : ArrayList<User> = ArrayList()
    var arrayUsersDB : ArrayList<User> = ArrayList()
    lateinit var adapter : RecyclerAdapeter
    lateinit var recyclerView: RecyclerView
    lateinit var binding : ActivityMainBinding
    lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        /*if (!binding.editTextSearch.isEmpty()){
            binding.recyclerViewSearchResults.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }*/
        recyclerView = binding.recyclerViewSearchResults
        dataBaseHelper = DataBaseHelper(this)
        arrayUsersDB = dataBaseHelper.getEveryone()!!
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        adapter  = RecyclerAdapeter(this, arrayUsersFilter)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                arrayUsersFilter.clear()
            }

            override fun afterTextChanged(s: Editable) {
                val search: String  = s.toString()
                if(search.isEmpty()){
                    arrayUsersFilter.clear()
                    adapter.notifyDataSetChanged()

                }else {
                Log.d("json", "onCreate: Filter initializes")
                    if (arrayUsersDB.isEmpty()){
                        for (i in 0 until arrayUsers.size ){
                            if(arrayUsers[i].name.toString().contains(search))
                            {
                                arrayUsersFilter.add(arrayUsers[i])
                                dataBaseHelper.addOne(arrayUsers[i])
                            }
                        }
                    }
                    else if (!arrayUsersDB.isEmpty()){
                        for (i in 0 until arrayUsersDB.size ){
                            if(arrayUsersDB[i].name.toString().contains(search))
                            {
                                arrayUsersFilter.add(arrayUsersDB[i])
                                Log.d("json", "onCreate: Encontrado en DB")                            }
                        }
                    } else {
                        for (i in 0 until arrayUsers.size ){
                            if(arrayUsers[i].name.toString().contains(search))
                            {
                                arrayUsersFilter.add(arrayUsers[i])
                                Log.d("json", "onCreate: Encontrado en WEB SERVICE")
                            }
                        }
                    }
            }}
        })
        getData()


    }
    fun getData() {
        val queue = MySingleton.getInstance(this).requestQueue
        val progressDialog: ProgressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading..")
        progressDialog.show()
        Log.d("json", "onCreate: " + arrayUsers.size)
        val jsonArrayRequest= JsonArrayRequest(0,
            (userRepo.URL_BASE+userRepo.GET_USERS),
            null,
            { response: JSONArray ->
                for (i in 0 until response.length()) {
                    try {
                        val jsonObject = response.getJSONObject(i)
                        arrayUsers.add(User(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("phone"),
                            jsonObject.getString("email")
                        ))

                        Log.d("json", "onCreate: " + jsonObject.getString("name"))
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                adapter.notifyDataSetChanged()
                progressDialog.dismiss()

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