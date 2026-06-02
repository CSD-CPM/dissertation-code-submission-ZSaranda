package com.example.gosti_backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.File

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {


        val uploadDir = File("uploads").absolutePath

        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:$uploadDir/")
    }
}