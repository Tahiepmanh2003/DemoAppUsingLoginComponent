package com.example.demoappusinglogincomponent.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoappusinglogincomponent.adapter.FoodPopularAdapter.FoodPopularViewHolder
import com.example.demoappusinglogincomponent.databinding.ItemFoodPopularBinding
import com.example.demoappusinglogincomponent.listener.IOnClickFoodItemListener
import com.example.demoappusinglogincomponent.model.Food
import com.example.demoappusinglogincomponent.utils.GlideUtils
import com.example.demoappusinglogincomponent.utils.StringFormatUtils

class FoodPopularAdapter(
    private val mListFoods: MutableList<Food>?,
    private val iOnClickFoodItemListener: IOnClickFoodItemListener?
) : RecyclerView.Adapter<FoodPopularViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPopularViewHolder {
        val itemFoodPopularBinding = ItemFoodPopularBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodPopularViewHolder(itemFoodPopularBinding)
    }

    override fun onBindViewHolder(holder: FoodPopularViewHolder, position: Int) {
        val food = mListFoods?.get(position) ?: return

        // Load ảnh banner
        GlideUtils.loadUrlBanner(food.getBanner(),
            holder.mItemFoodPopularBinding.imageFood as ImageView
        )

        // Hiển thị giảm giá
        if (food.getSale() <= 0) {
            holder.mItemFoodPopularBinding.tvSaleOff.visibility = View.GONE
        } else {
            holder.mItemFoodPopularBinding.tvSaleOff.visibility = View.VISIBLE
            holder.mItemFoodPopularBinding.tvSaleOff.text =
                StringFormatUtils.formatSale(food.getSale())
        }

        // Xử lý click
        holder.mItemFoodPopularBinding.layoutItem.setOnClickListener {
            iOnClickFoodItemListener?.onClickItemFood(food)
        }
    }

    override fun getItemCount(): Int {
        return mListFoods?.size ?: 0
    }

    class FoodPopularViewHolder(val mItemFoodPopularBinding: ItemFoodPopularBinding) :
        RecyclerView.ViewHolder(mItemFoodPopularBinding.root)
}
