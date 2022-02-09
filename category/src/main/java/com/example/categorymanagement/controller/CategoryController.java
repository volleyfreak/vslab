package com.example.categorymanagement.controller;



import com.example.categorymanagement.exception.CategoryNotFoundException;
import com.example.categorymanagement.model.Category;

import com.example.categorymanagement.repository.CategoryRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

import java.util.Optional;

// Check this to do the real implementation https://spring.io/guides/tutorials/rest/
@RestController // This means that this class is a Controller
@RequestMapping(path="/")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(path="/categories")
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping(path="/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addNewCategory (@RequestBody Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    @GetMapping("/categories/{id}")
    public Category getOneCategory(@PathVariable int id) {
        return categoryRepository.findById(id).
                orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable int id) {
        try {
            new RestTemplate().delete("http://product:8082//categories/{id}", id);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
        categoryRepository.deleteById(id);
    }
}
