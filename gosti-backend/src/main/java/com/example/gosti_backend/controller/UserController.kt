package com.example.gosti_backend.controller


import com.example.gosti_backend.dto.LoginRequestDto
import com.example.gosti_backend.model.User
import com.example.gosti_backend.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        return try {
            val newUser = userService.createUser(user)
            ResponseEntity.status(HttpStatus.CREATED).body(newUser)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<User> {
        val user = userService.getUserByEmail(loginRequest.email)
        return if (user != null && user.password == loginRequest.password) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }
}

