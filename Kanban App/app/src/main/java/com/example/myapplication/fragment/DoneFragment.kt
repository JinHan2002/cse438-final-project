package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.adapter.CustomAdapterDoing
import com.example.myapplication.adapter.CustomAdapterDone
import com.example.myapplication.model.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_doing.*
import kotlinx.android.synthetic.main.fragment_done.*
import java.util.ArrayList
import java.util.HashMap

@SuppressLint("ValidFragment")

class DoneFragment (val mContext: Context): Fragment() {
    var tasks = ArrayList<Task>()
    var adapter = CustomAdapterDone(tasks, mContext,2)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_done, container, false)

    }
    override fun onStart() {
        super.onStart()
        favorites_list_done.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        update()

    }

    fun update(){
        val userId = App.firebaseAuth?.currentUser?.uid
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId!!).addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException == null && documentSnapshot != null) {
                tasks = ArrayList()
                val userData = documentSnapshot.data

                val data = userData?.get("done") as? ArrayList<HashMap<String, Any>>

                if (data == null) {
                    //TODO: new user

                } else {
                    for (i in data) {
                        val newTask =
                            Task(i.get("date").toString(), i.get("title").toString(), i.get("body").toString(),i.get("photo").toString())
                        tasks.add(newTask)
                    }
                }

                activity?.runOnUiThread {
                    adapter = CustomAdapterDone(tasks,mContext,2)
                    if(favorites_list_done!=null) {
                        favorites_list_done.adapter = adapter
                    }
                }



            } else {
                Toast.makeText(mContext, "Unable to get score", Toast.LENGTH_SHORT).show()
            }
        }


    }

}