package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentManagerAdapter
import com.example.myapplication.model.commentModel
import com.google.firebase.database.*

class manageReview : AppCompatActivity() {

    private lateinit var userCmtRecycleView: RecyclerView
    private lateinit var userCmtList: ArrayList<commentModel>
    private lateinit var dbURef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_review)

        userCmtRecycleView = findViewById(R.id.rvucomments)
        userCmtRecycleView.layoutManager = LinearLayoutManager(this)
        userCmtRecycleView.setHasFixedSize(true)

        userCmtList = arrayListOf<commentModel>()

        getUserComments()
    }
    private fun getUserComments(){

        dbURef = FirebaseDatabase.getInstance().getReference("comments")

        dbURef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userCmtList.clear()
                if(snapshot.exists()){
                    for(ucmtSnap in snapshot.children){
                        val userCmtData =  ucmtSnap.getValue(commentModel::class.java)
                        userCmtList.add(userCmtData!!)
                    }
                    val ucAdapter = CommentManagerAdapter(userCmtList)
                    userCmtRecycleView.adapter = ucAdapter

                    ucAdapter.setOnItemClickListener(object : CommentManagerAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@manageReview, Edit_Review::class.java)

                            intent.putExtra("newComment",userCmtList[position].newComment)
                            startActivity(intent)
                        }

                    })
                    userCmtRecycleView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}