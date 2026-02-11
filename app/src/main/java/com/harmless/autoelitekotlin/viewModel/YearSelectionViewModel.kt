package com.harmless.autoelitekotlin.viewModel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class YearSelectionViewModel {




    interface YearsCallback {
        fun onYearsLoaded(years: List<Int>)
        fun onCancelled(error: DatabaseError)
    }


        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

        fun loadYears(callback: YearsCallback) {
            val yearsReference: DatabaseReference = databaseReference.child("years")

            val yearsListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val yearsList = snapshot.children.mapNotNull { it.getValue(Int::class.java) }
                    callback.onYearsLoaded(yearsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onCancelled(error)
                }
            }

            yearsReference.addValueEventListener(yearsListener)
        }


    }



