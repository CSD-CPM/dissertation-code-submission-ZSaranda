package com.example.gosti_backend.service


import com.example.gosti_backend.model.AllMenu
import com.example.gosti_backend.repository.MenuRepository
import org.springframework.stereotype.Service

@Service
class MenuService(private val menuRepository: MenuRepository) {

    fun getAllMenuItems(): List<AllMenu> = menuRepository.findAll()

    fun getMenuItemById(id: Long): AllMenu? = menuRepository.findById(id).orElse(null)

    fun createMenuItem(menuItem: AllMenu): AllMenu = menuRepository.save(menuItem)

    fun updateMenuItem(id: Long, updatedMenu: AllMenu): AllMenu? {
        val existingMenu = menuRepository.findById(id).orElse(null) ?: return null
        existingMenu.foodName = updatedMenu.foodName
        existingMenu.foodPrice = updatedMenu.foodPrice
        existingMenu.foodDescription = updatedMenu.foodDescription
        existingMenu.foodImage = updatedMenu.foodImage
        existingMenu.foodIngredient = updatedMenu.foodIngredient
        existingMenu.category = updatedMenu.category
        return menuRepository.save(existingMenu)
    }

    fun deleteMenuItem(id: Long) {
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id)
        } else {
            throw IllegalArgumentException("Menu item not found")
        }
    }

    fun getMenuItemsByCategory(category: String): List<AllMenu> = menuRepository.findByCategory(category)
}
