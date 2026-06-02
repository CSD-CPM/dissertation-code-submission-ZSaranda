package com.example.gosti_backend.service

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class QRCodeService {

    fun generateQRCode(restaurantId: Long, tableNumber: Int): ByteArray {


        val content = "gosti://table?restaurantId=$restaurantId&tableNumber=$tableNumber"

       
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(
            content,
            BarcodeFormat.QR_CODE,
            300,
            300
        )


        val image = MatrixToImageWriter.toBufferedImage(bitMatrix)


        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "PNG", outputStream)

        return outputStream.toByteArray()
    }
}