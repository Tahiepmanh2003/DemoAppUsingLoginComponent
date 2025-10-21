package com.example.demoappusinglogincomponent.view.main

import android.content.Context
import com.example.demoappusinglogincomponent.ControllerApplication
import com.example.demoappusinglogincomponent.constant.GlobalFuntion
import com.example.demoappusinglogincomponent.model.Food
import com.example.demoappusinglogincomponent.utils.StringUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class HomePresenter(private val mHomeMVPView: HomeMVPView?) {

    fun getListFoodFromFirebase(context: Context?, key: String?) {
        if (context == null) {
            return
        }
        ControllerApplication.get(context).getFoodDatabaseReference()
                ?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list: MutableList<Food> = ArrayList()
                        for (dataSnapshot in snapshot.children) {
                            val food: Food = dataSnapshot.getValue<Food>(Food::class.java) ?: return
                            if (StringUtil.isEmpty(key)) {
                                list.add(0, food)
                            } else {
                                if (GlobalFuntion.getTextSearch(food.getName()!!).trim().toLowerCase(Locale.getDefault())
                                                .contains(GlobalFuntion.getTextSearch(key!!).trim().toLowerCase(Locale.getDefault()))) {
                                    list.add(0, food)
                                }
                            }
                        }
                        mHomeMVPView?.loadListFoodSuccess(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        mHomeMVPView?.loadListFoodError()
                    }
                })
    }

    fun getListFoodPopular(listFood: MutableList<Food>?): MutableList<Food> {
        val list: MutableList<Food> = ArrayList()
        if (listFood == null || listFood.isEmpty()) {
            return list
        }
        for (food in listFood) {
            if (food.isPopular()) {
                list.add(food)
            }
        }
        return list
    }
}