package com.example.gosti_backend.controller


import com.example.gosti_backend.model.AllMenu
import com.example.gosti_backend.service.MenuService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/menu")
class MenuController(private val menuService: MenuService) {

    @GetMapping
    fun getAllMenuItems(): List<AllMenu> = menuService.getAllMenuItems()

    @GetMapping("/{id}")
    fun getMenuItemById(@PathVariable id: Long): ResponseEntity<AllMenu> {
        val menu = menuService.getMenuItemById(id)
        return if (menu != null) ResponseEntity.ok(menu)
        else ResponseEntity.notFound().build()
    }

    @PostMapping("/add")
    fun createMenuItem(@RequestBody menuItem: AllMenu): ResponseEntity<AllMenu> {
        val newMenu = menuService.createMenuItem(menuItem)
        return ResponseEntity.status(HttpStatus.CREATED).body(newMenu)
    }

    @PutMapping("/{id}")
    fun updateMenuItem(@PathVariable id: Long, @RequestBody menuItem: AllMenu): ResponseEntity<AllMenu> {
        val updated = menuService.updateMenuItem(id, menuItem)
        return if (updated != null) ResponseEntity.ok(updated)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteMenuItem(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            menuService.deleteMenuItem(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/category/{category}")
    fun getMenuItemsByCategory(@PathVariable category: String): List<AllMenu> =
            menuService.getMenuItemsByCategory(category)
}
