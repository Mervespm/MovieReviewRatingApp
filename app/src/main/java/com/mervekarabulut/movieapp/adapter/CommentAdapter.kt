package com.mervekarabulut.movieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mervekarabulut.movieapp.R
import com.mervekarabulut.movieapp.model.Comment

class CommentAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item_layout, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentList[position]
        holder.bind(currentComment)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val commentTextView: TextView = itemView.findViewById(R.id.commentTextView)
        private val userEmailTextView: TextView = itemView.findViewById(R.id.userEmailTextView)
        private val movieNameTextView: TextView = itemView.findViewById(R.id.movieNameTextView)

        fun bind(comment: Comment) {
            val rating = comment.rating?.toFloat() // Convert the integer rating to float
            if (rating != null) {
                ratingBar.rating = rating
            }
            commentTextView.text = comment.comment
            userEmailTextView.text = comment.userEmail
            movieNameTextView.text = comment.movieName
        }
    }


}
