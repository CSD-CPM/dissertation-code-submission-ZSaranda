package com.example.gosti.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gosti.Adapter.MenuAdapter
import com.example.gosti.Model.MenuItem
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomSheetBinding
    private lateinit var menuItems: MutableList<MenuItem>

    // 🔥 NEW: QR CONTEXT
    private var restaurantId: String? = null
    private var tableNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        // ================= GET QR DATA =================
        restaurantId = arguments?.getString("restaurantId")
        tableNumber = arguments?.getString("tableNumber")

        // ================= UI =================
        binding.buttonBack.setOnClickListener {
            dismiss()
        }



        retrieveMenuItems()

        return binding.root
    }

    // ================= LOAD MENU =================
    private fun retrieveMenuItems() {

        menuItems = mutableListOf()

        if (restaurantId == null) {
            Log.e("MenuBottomSheet", "Restaurant ID is NULL")
            return
        }

        RetrofitInstance.menuApi.getMenuByRestaurant(restaurantId!!.toLong())
            .enqueue(object : Callback<List<MenuItem>> {

                override fun onResponse(
                    call: Call<List<MenuItem>>,
                    response: Response<List<MenuItem>>
                ) {
                    if (response.isSuccessful) {

                        response.body()?.let {
                            menuItems.clear()
                            menuItems.addAll(it)

                            if (isAdded && context != null) {
                                setAdapter()
                            }
                        }

                    } else {
                        Log.e("MenuBottomSheet", "Failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                    Log.e("MenuBottomSheet", "Error: ${t.localizedMessage}")
                }
            })
    }

    // ================= SET ADAPTER =================
    private fun setAdapter() {

        if (menuItems.isNotEmpty() && isAdded && context != null) {

            val adapter = MenuAdapter(menuItems, requireContext())

            binding.menuRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())

            binding.menuRecyclerView.adapter = adapter
        }
    }

    companion object {


        fun newInstance(restaurantId: String, tableNumber: String): MenuBottomSheetFragment {

            val fragment = MenuBottomSheetFragment()

            val args = Bundle().apply {
                putString("restaurantId", restaurantId)
                putString("tableNumber", tableNumber)
            }

            fragment.arguments = args

            return fragment
        }
    }
}