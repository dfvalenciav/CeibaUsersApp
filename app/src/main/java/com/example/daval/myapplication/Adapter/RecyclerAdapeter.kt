package com.example.daval.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.daval.myapplication.R
import com.example.daval.myapplication.models.User
import com.example.daval.myapplication.views.PostActivity

class RecyclerAdapeter (private val context: Context, private val  arrayUsersAdapter : ArrayList<User>) : RecyclerView.Adapter<RecyclerAdapeter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.user_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapeter.MyViewHolder, position: Int) {
        val name = arrayUsersAdapter.get(position).name
        val phone = arrayUsersAdapter.get(position).phone
        Log.d("json", "onCreate: $phone")
        val email = arrayUsersAdapter.get(position).email

        holder.userName.setText(name)
        holder.userPhone.setText(phone)
        holder.userEmail.setText(email)
        holder.userButtonPost.setOnClickListener {
            val intent:Intent = Intent(context, PostActivity::class.java)
            val userId : Int = arrayUsersAdapter.get(position).id
            intent.putExtra("userId", userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = arrayUsersAdapter.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView
        val userPhone: TextView
        val userEmail: TextView
        val userButtonPost : Button

        init {
            userName = itemView.findViewById(R.id.namePost)
            userPhone = itemView.findViewById(R.id.phone)
            userEmail = itemView.findViewById(R.id.email)
            userButtonPost = itemView.findViewById(R.id.btn_view_post)
            itemView.setOnClickListener {

            }
        }
    }
}