package com.example.demoappusinglogincomponent.view.main

import com.example.demoappusinglogincomponent.model.Food

interface HomeMVPView {
    fun loadListFoodSuccess(list: MutableList<Food>)
    fun loadListFoodError()
}