package com.example.demoappusinglogincomponent.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.demoappusinglogincomponent.databinding.ItemFoodGridBinding
import com.example.demoappusinglogincomponent.listener.IOnClickFoodItemListener
import com.example.demoappusinglogincomponent.model.Food
import com.example.demoappusinglogincomponent.utils.GlideUtils
import com.example.demoappusinglogincomponent.utils.StringFormatUtils

class FoodGridAdapter(
    private val iOnClickFoodItemListener: IOnClickFoodItemListener?
) : RecyclerView.Adapter<FoodGridAdapter.FoodGridViewHolder>() {

    // DiffUtil callback để so sánh phần tử
    private val differCallback = object : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.getId() == newItem.getId()
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.getName() == newItem.getName()
                    && oldItem.getImage() == newItem.getImage()
                    && oldItem.getPrice() == newItem.getPrice()
                    && oldItem.getSale() == newItem.getSale()
                    && oldItem.getDescription() == newItem.getDescription()
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodGridViewHolder {
        val binding = ItemFoodGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FoodGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodGridViewHolder, position: Int) {
        val food = differ.currentList.getOrNull(position) ?: return

        // Load hình ảnh
        GlideUtils.loadUrl(food.getImage(), holder.binding.imgFood)

        if (food.getSale() <= 0) {
            holder.binding.tvSaleOff.visibility = View.GONE
            holder.binding.tvPrice.visibility = View.GONE
            holder.binding.tvPriceSale.text = StringFormatUtils.formatCurrency(food.getPrice())
        } else {
            holder.binding.tvSaleOff.visibility = View.VISIBLE
            holder.binding.tvPrice.visibility = View.VISIBLE

            holder.binding.tvSaleOff.text = StringFormatUtils.formatSale(food.getSale())

            holder.binding.tvPrice.text = StringFormatUtils.formatCurrency(food.getPrice())
            holder.binding.tvPrice.paintFlags =
                holder.binding.tvPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.binding.tvPriceSale.text =
                StringFormatUtils.formatCurrency(food.getRealPrice())
        }

        holder.binding.tvFoodName.text = food.getName()

        holder.binding.layoutItem.setOnClickListener {
            iOnClickFoodItemListener?.onClickItemFood(food)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    class FoodGridViewHolder(val binding: ItemFoodGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Cập nhật dữ liệu bằng DiffUtil
    fun updateData(newList: List<Food>) {
        differ.submitList(newList)
    }

    // Hàm tìm kiếm trực tiếp trong Adapter
    fun searchFoods(query: String, allFoods: List<Food>) {
        if (query.isBlank()) {
            updateData(allFoods)
            android.util.Log.d("FoodGridAdapter", "Search rỗng → trả về toàn bộ (${allFoods.size})")
        } else {
            val filteredList = allFoods.filter { food ->
                food.getName()?.contains(query, ignoreCase = true) == true
            }
            android.util.Log.d("FoodGridAdapter", "Search='$query' → filtered=${filteredList.size}")
            updateData(filteredList)
        }
    }

}
