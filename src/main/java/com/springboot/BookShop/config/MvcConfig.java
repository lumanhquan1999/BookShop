package com.springboot.BookShop.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		Path bookUploadDir = Paths.get("./book-images");
		String bookUploadPath = bookUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/book-images/**").addResourceLocations("file:/" + bookUploadPath + "/");
	}

}
