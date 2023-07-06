package com.mervekarabulut.movieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mervekarabulut.movieapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.mervekarabulut.movieapp.adapter.CommentAdapter
import com.mervekarabulut.movieapp.model.Comment

class CommentList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private val commentList: MutableList<Comment> = mutableListOf()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    val intent = Intent(this, FeedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.search -> {
                    val intent = Intent(this, CommentList::class.java)
                    startActivity(intent)
                    true
                }
                R.id.person -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        recyclerView = findViewById(R.id.commentRecyclerView)
        commentAdapter = CommentAdapter(commentList)
        recyclerView.adapter = commentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        retrieveCommentsFromFirestore()
    }

    private fun retrieveCommentsFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val commentsCollection = firestore.collection("Comments")

        commentsCollection.get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                val comment = document.toObject(Comment::class.java)
                comment?.let {
                    commentList.add(it)
                }
            }
            commentAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            // Handle failure case
        }
    }
}
