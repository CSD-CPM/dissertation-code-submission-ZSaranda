package com.example.gosti_backend.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/api/upload")
class UploadController {

    @PostMapping
    fun uploadImage(@RequestParam file: MultipartFile): ResponseEntity<String> {

        val fileName = System.currentTimeMillis().toString() + ".jpg"


        val uploadPath = Paths.get("uploads")

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }

        val path = uploadPath.resolve(fileName)

        Files.copy(file.inputStream, path)


        val imageUrl = "http://192.168.1.19:8080/uploads/$fileName"

        return ResponseEntity.ok(imageUrl)
    }
}