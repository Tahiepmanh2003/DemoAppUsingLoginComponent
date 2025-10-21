package com.example.demoappusinglogincomponent.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.demoappusinglogincomponent.BaseFragment
import com.example.demoappusinglogincomponent.MainActivity
import com.example.demoappusinglogincomponent.R
import com.example.demoappusinglogincomponent.adapter.FoodGridAdapter
import com.example.demoappusinglogincomponent.adapter.FoodPopularAdapter
import com.example.demoappusinglogincomponent.constant.Constant
import com.example.demoappusinglogincomponent.constant.GlobalFuntion
import com.example.demoappusinglogincomponent.view.main.HomeMVPView
import com.example.demoappusinglogincomponent.databinding.FragmentHomeBinding
import com.example.demoappusinglogincomponent.listener.IOnClickFoodItemListener
import com.example.demoappusinglogincomponent.model.Food
import com.example.demoappusinglogincomponent.view.main.HomePresenter
//import com.example.demoappusinglogincomponent.view.chatbot.ChatbotActivity
//import com.example.demoappusinglogincomponent.view.food_detail.FoodDetailActivity
import com.google.android.material.appbar.MaterialToolbar

class HomeFragment : BaseFragment(), HomeMVPView {

    private var mHomePresenter: HomePresenter? = null
    private var mBinding: FragmentHomeBinding? = null
    private var mListFoodAll: MutableList<Food> = mutableListOf()
    private var mListFoodPopular: MutableList<Food>? = null
    private var foodGridAdapter: FoodGridAdapter? = null
    private var isSearching: Boolean = false

    private val mHandlerBanner = Handler(Looper.getMainLooper())
    private val mRunnableBanner = Runnable {
        if (mListFoodPopular.isNullOrEmpty()) return@Runnable
        val vp = mBinding?.viewpager2 ?: return@Runnable
        vp.currentItem = if (vp.currentItem == mListFoodPopular!!.size - 1) 0 else vp.currentItem + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        mHomePresenter = HomePresenter(this)
        mHomePresenter?.getListFoodFromFirebase(activity, "")

        setupChatBot()

        return mBinding!!.root
    }

    private fun setupChatBot() {
        mBinding?.fabChatbot?.setOnClickListener {
//            val intent = Intent(requireActivity(), ChatbotActivity::class.java)
//            // Truyền danh sách món ăn cho ChatBot để có context
//            intent.putExtra("FOOD_LIST", ArrayList(mListFoodAll))
//            startActivity(intent)
        }
    }

    override fun initToolbar() {
        val toolbar: MaterialToolbar? = mBinding?.toolbar
        toolbar?.title = getString(R.string.home)
        toolbar?.setNavigationIcon(R.drawable.ic_menu)
        toolbar?.setNavigationOnClickListener {
//            (activity as? MainActivity)?.openDrawer()
        }

        val menu = toolbar?.menu
        val searchItem = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            val searchView = searchItem.actionView as? SearchView
            if (searchView != null) {
                searchView.queryHint = getString(R.string.hint_search_name)
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        filterFoods(query.orEmpty())
                        searchView.clearFocus()
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        filterFoods(newText.orEmpty())
                        return true
                    }
                })
            }
        }
    }

    private fun filterFoods(query: String) {
        // Kiểm tra dữ liệu gốc
        if (mListFoodAll.isEmpty()) {
            Log.d("HomeFragment", "mListFoodAll đang rỗng, chưa thể filter")
            return
        }

        foodGridAdapter?.searchFoods(query, mListFoodAll)
        isSearching = query.isNotEmpty()

        // Log số lượng kết quả filter
        Log.d(
            "HomeFragment",
            "Filter query='$query' → kết quả=${foodGridAdapter?.itemCount}"
        )

        mBinding?.tvNoResult?.visibility =
            if (foodGridAdapter?.itemCount == 0) View.VISIBLE else View.GONE

        displayListFoodPopular()
    }

    private fun displayListFoodPopular() {
        if (!isSearching) {
            mListFoodPopular = mHomePresenter?.getListFoodPopular(mListFoodAll)
            val adapterPopular = FoodPopularAdapter(mListFoodPopular, object : IOnClickFoodItemListener {
                override fun onClickItemFood(food: Food) {
                    goToFoodDetail(food)
                }
            })
            mBinding?.viewpager2?.adapter = adapterPopular
            mBinding?.indicator3?.setViewPager(mBinding?.viewpager2)
            mBinding?.viewpager2?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mHandlerBanner.removeCallbacks(mRunnableBanner)
                    mHandlerBanner.postDelayed(mRunnableBanner, 3000)
                }
            })
            mBinding?.viewpager2?.visibility = View.VISIBLE
            mBinding?.indicator3?.visibility = View.VISIBLE
        } else {
            mBinding?.viewpager2?.visibility = View.GONE
            mBinding?.indicator3?.visibility = View.GONE
        }
    }

    private fun displayListFoodSuggest() {
        if (foodGridAdapter == null) {
            val gridLayoutManager = GridLayoutManager(activity, 2)
            mBinding?.rcvFood?.layoutManager = gridLayoutManager
            foodGridAdapter = FoodGridAdapter(object : IOnClickFoodItemListener {
                override fun onClickItemFood(food: Food) {
                    goToFoodDetail(food)
                }
            })
            mBinding?.rcvFood?.adapter = foodGridAdapter
        }
        foodGridAdapter?.updateData(mListFoodAll)
    }

    private fun goToFoodDetail(food: Food) {
//        val bundle = Bundle()
//        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food)
//        GlobalFuntion.startActivity(requireActivity(), FoodDetailActivity::class.java, bundle)
    }

    override fun loadListFoodSuccess(list: MutableList<Food>) {
        mListFoodAll = list.toMutableList()
        mBinding?.layoutContent?.visibility = View.VISIBLE
        isSearching = false
        mBinding?.tvNoResult?.visibility = View.GONE
        displayListFoodPopular()
        displayListFoodSuggest()
    }

    override fun loadListFoodError() {
        GlobalFuntion.showToastMessage(activity, getString(R.string.msg_get_date_error))
    }

    override fun onPause() {
        super.onPause()
        mHandlerBanner.removeCallbacks(mRunnableBanner)
    }

    override fun onResume() {
        super.onResume()
        mHandlerBanner.postDelayed(mRunnableBanner, 3000)
    }
}