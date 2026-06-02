package com.example.gosti_backend.controller

import com.example.gosti_backend.service.QRCodeService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/qr")
class QRCodeController(
        private val qrCodeService: QRCodeService
) {

    @GetMapping("/{restaurantId}/{tableNumber}", produces = [MediaType.IMAGE_PNG_VALUE])
    fun generateQR(
            @PathVariable restaurantId: Long,
            @PathVariable tableNumber: Int
    ): ResponseEntity<ByteArray> {

        val qrImage = qrCodeService.generateQRCode(restaurantId, tableNumber)

        return ResponseEntity.ok(qrImage)
    }
}