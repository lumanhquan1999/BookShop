package com.springboot.BookShop.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		Path bookUploadDir = Paths.get("./book-images");
		String bookUploadPath = bookUploadDir.toFile().getAbsolutePath();
		
		Path avatarUploadDir = Paths.get("./avatar-images");
		String avatarUploadPath = avatarUploadDir.toFile().getAbsolutePath();
		
		registry.addResourceHandler("/book-images/**").addResourceLocations("file:/" + bookUploadPath + "/");
		registry.addResourceHandler("/avatar-images/**").addResourceLocations("file:/" + avatarUploadPath + "/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		
		registry.addViewController("/books/list").setViewName("list-books");
	}

}
