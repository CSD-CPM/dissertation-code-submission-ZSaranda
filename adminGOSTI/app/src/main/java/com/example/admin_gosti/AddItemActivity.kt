package com.example.admin_gosti

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.admin_gosti.databinding.ActivityAddItemBinding
import com.example.admin_gosti.model.AllMenu
import com.example.admin_gosti.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class AddItemActivity : AppCompatActivity() {

    private var foodImageUri: Uri? = null

    private val binding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    private val menuApi by lazy {
        ApiClient.menuApi
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.AddItemButton.setOnClickListener {
            addItem()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            foodImageUri = uri
            binding.selectedImage.setImageURI(uri)
        }

    private fun addItem() {

        val foodName = binding.foodName.text.toString().trim()
        val foodPrice = binding.foodPrice.text.toString().trim()
        val foodDescription = binding.description.text.toString().trim()
        val foodIngredient = binding.ingredint.text.toString().trim()

        if (foodName.isEmpty() || foodPrice.isEmpty() || foodImageUri == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        binding.AddItemButton.isEnabled = false

        lifecycleScope.launch {

            try {
                // 1. Convert file
                val file = uriToFile(foodImageUri!!)

                val requestFile = RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    file
                )

                val body = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    requestFile
                )


                val uploadResponse = withContext(Dispatchers.IO) {
                    menuApi.uploadImage(body)
                }

                if (!uploadResponse.isSuccessful) {
                    Toast.makeText(this@AddItemActivity, "Image upload failed", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val imageUrl = uploadResponse.body()?.string() ?: ""


                val newItem = AllMenu(
                    foodName = foodName,
                    foodPrice = foodPrice,
                    foodDescription = foodDescription,
                    foodIngredient = foodIngredient,
                    foodImage = imageUrl
                )

                val response = menuApi.createMenuItem(newItem)

                if (response.isSuccessful) {
                    Toast.makeText(this@AddItemActivity, "Item added", Toast.LENGTH_SHORT).show()
                    clearForm()
                } else {
                    Toast.makeText(this@AddItemActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("AddItemActivity", "Error", e)
                Toast.makeText(this@AddItemActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }

            binding.AddItemButton.isEnabled = true
        }
    }

    private fun clearForm() {
        binding.foodName.text.clear()
        binding.foodPrice.text.clear()
        binding.description.text.clear()
        binding.ingredint.text.clear()

        foodImageUri = null
        binding.selectedImage.setImageDrawable(null)
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)!!
        val file = File(cacheDir, "upload_image.jpg")
        val outputStream = FileOutputStream(file)

        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file
    }
}