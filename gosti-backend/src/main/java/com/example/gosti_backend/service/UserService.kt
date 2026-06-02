package com.example.gosti_backend.service


import com.example.gosti_backend.model.User
import com.example.gosti_backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Long): User? = userRepository.findById(id).orElse(null)

    fun createUser(user: User): User {
        // Optional: check if email already exists
        userRepository.findByEmail(user.email)?.let {
            throw IllegalArgumentException("Email already registered")
        }
        return userRepository.save(user)
    }

    fun updateUser(id: Long, updatedUser: User): User? {
        val existingUser = userRepository.findById(id).orElse(null) ?: return null
        existingUser.name = updatedUser.name
        existingUser.nameOfRestaurant = updatedUser.nameOfRestaurant
        existingUser.email = updatedUser.email
        existingUser.password = updatedUser.password
        existingUser.address = updatedUser.address
        existingUser.phone = updatedUser.phone
        existingUser.role = updatedUser.role
        return userRepository.save(existingUser)
    }

    fun deleteUser(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else {
            throw IllegalArgumentException("User not found")
        }
    }

    fun getUserByEmail(email: String): User? = userRepository.findByEmail(email)
}
