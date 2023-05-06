package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.model.commentModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class addComment : AppCompatActivity() {

    private lateinit var comment: EditText
    private lateinit var btnSave: Button
    private lateinit var btnManage: Button

    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        comment = findViewById(R.id.comment)
        btnSave = findViewById(R.id.sumbitbutton)
        btnManage = findViewById(R.id.btnreviweComment)

        dbRef = FirebaseDatabase.getInstance().getReference("comments")

        btnSave.setOnClickListener{
            saveComments()
        }


        btnManage.setOnClickListener {
            val intent = Intent(this, manageReview ::class.java)
            startActivity(intent)
        }


    }

    private fun saveComments() {

        //getting comment
        val newComment = comment.text.toString()

        if (newComment.isEmpty()){
            comment.error = "Please Enter Comment"
            Toast.makeText(this, "Please Enter Comment", Toast.LENGTH_LONG).show()
            return
        }

        val commentId = dbRef.push().key!!

        val shopComment = commentModel (commentId,newComment)

        dbRef.child(commentId).setValue(shopComment).addOnCompleteListener{
            Toast.makeText(this, "Comment Added Successfully", Toast.LENGTH_LONG).show()
            comment.text.clear()
                val intent = Intent(this, manageReview::class.java)
                startActivity(intent)


        }.addOnFailureListener { err->
            Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
        }
    }
}