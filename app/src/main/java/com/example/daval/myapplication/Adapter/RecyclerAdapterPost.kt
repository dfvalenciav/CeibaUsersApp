package com.example.daval.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.daval.myapplication.R
import com.example.daval.myapplication.models.Post
import com.example.daval.myapplication.models.User

class RecyclerAdapterPost(private val context: Context, private val  arrayPostAdapter : ArrayList<Post>) :
    RecyclerView.Adapter<RecyclerAdapterPost.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterPost.MyViewHolder {
        val view: View
        val mInflater = LayoutInflater.from(context)
        view = mInflater.inflate(R.layout.post_list_item, parent, false)
        return RecyclerAdapterPost.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterPost.MyViewHolder, position: Int) {
       val title = arrayPostAdapter.get(position).title
        val body = arrayPostAdapter.get(position).body

        holder.postTitle.setText(title)
        holder.postBody.setText(body)
    }

    override fun getItemCount() = arrayPostAdapter.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postTitle: TextView
        val postBody: TextView
        init {
            postTitle = itemView.findViewById(R.id.title)
            postBody = itemView.findViewById(R.id.body)
            itemView.setOnClickListener {

            }
        }
    }
}