package com.example.gosti.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.gosti.Adapter.PopularAdapter
import com.example.gosti.Model.MenuItem
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.FragmentHomeBinding
import com.example.gosti.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "MenuBottomSheet")
        }

        setupImageSlider()
        retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun setupImageSlider() {
        val imageList = arrayListOf(
            SlideModel(R.drawable.banner1, ScaleTypes.FIT),
            SlideModel(R.drawable.banner2, ScaleTypes.FIT),
            SlideModel(R.drawable.banner3, ScaleTypes.FIT)
        )
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
    }

    private fun retrieveAndDisplayPopularItems() {
        menuItems = mutableListOf()

        val call = RetrofitInstance.menuApi.getAllMenuItems()

        call.enqueue(object : Callback<List<MenuItem>> {
            override fun onResponse(
                call: Call<List<MenuItem>>,
                response: Response<List<MenuItem>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        menuItems.addAll(it)
                        displayPopularItems()
                    }
                }
            }

            override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                // Optionally show error message
            }
        })
    }

    private fun displayPopularItems() {
        if (menuItems.isEmpty()) return

        val popularItems = menuItems.shuffled().take(6)
        val adapter = PopularAdapter(popularItems, requireContext())
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopulerRecyclerView.adapter = adapter
    }
}