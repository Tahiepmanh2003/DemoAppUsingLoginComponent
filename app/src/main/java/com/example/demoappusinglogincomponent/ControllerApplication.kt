package com.example.demoappusinglogincomponent

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ControllerApplication : Application() {

    private var mFirebaseDatabase: FirebaseDatabase? = null

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        mFirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    }

    fun getFoodDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("/food")
    }

    fun getFeedbackDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("/feedback")
    }

    fun getBookingDatabaseReference(): DatabaseReference? {
        return mFirebaseDatabase?.getReference("/booking")
    }

    companion object {
        operator fun get(context: Context?): ControllerApplication {
            return context?.applicationContext as ControllerApplication
        }
        private const val FIREBASE_URL = "https://foodordermvp-e4f07-default-rtdb.firebaseio.com"
    }
}