package com.example.gosti.Fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup as VG
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gosti.Adapter.MenuAdapter
import com.example.gosti.Model.MenuItem
import com.example.gosti.Network.RetrofitInstance
import com.example.gosti.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private val originalMenuItems = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Expand & activate SearchView properly
        binding.searchView.isIconified = false
        binding.searchView.requestFocus()

        // Make text black without R
        binding.searchView.post {
            val searchEditText = findSearchEditText(binding.searchView)
            searchEditText?.apply {
                setTextColor(Color.BLACK)
                setHintTextColor(Color.BLACK)
                requestFocus()

                val imm = requireContext()
                    .getSystemService(InputMethodManager::class.java)
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        setupSearchView()
        retrieveMenuItemsFromApi()

        return binding.root
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenu(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenu(newText)
                return true
            }
        })
    }

    private fun retrieveMenuItemsFromApi() {
        RetrofitInstance.menuApi.getAllMenuItems()
            .enqueue(object : Callback<List<MenuItem>> {

                override fun onResponse(
                    call: Call<List<MenuItem>>,
                    response: Response<List<MenuItem>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            originalMenuItems.clear()
                            originalMenuItems.addAll(it)
                            setAdapter(originalMenuItems)
                        }
                    }
                }

                override fun onFailure(call: Call<List<MenuItem>>, t: Throwable) {
                    Log.e("SearchFragment", "Network error", t)
                }
            })
    }

    private fun filterMenu(query: String?) {
        val filtered = originalMenuItems.filter {
            it.foodName?.contains(query ?: "", ignoreCase = true) == true
        }
        setAdapter(filtered)
    }

    private fun setAdapter(list: List<MenuItem>) {
        adapter = MenuAdapter(list, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }

    /** Recursively find EditText inside SearchView (no R usage) */
    private fun findSearchEditText(view: View): EditText? {
        if (view is EditText) return view
        if (view is VG) {
            for (i in 0 until view.childCount) {
                val result = findSearchEditText(view.getChildAt(i))
                if (result != null) return result
            }
        }
        return null
    }
}
