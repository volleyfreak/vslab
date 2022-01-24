package com.example.categorymanagement.controller;



import com.example.categorymanagement.model.Category;
import com.example.categorymanagement.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

// Check this to do the real implementation https://spring.io/guides/tutorials/rest/
@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping(path="category") // Map ONLY POST Requests
    public @ResponseBody String addNewCategory (@RequestBody Category newCategory) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        //Category category = new Category();
        //category.setName(name);
        categoryRepository.save(newCategory);
        return "Saved";
    }

    @GetMapping(path="category")
    public @ResponseBody Iterable<Category> getAllCategories() {
        // This returns a JSON or XML with the users
        return categoryRepository.findAll();
    }

    @DeleteMapping(path="category")
    public @ResponseBody String DeleteCategory(@RequestBody Category category) {
        try {
            new RestTemplate().delete("http://product:8080/category/{name}", category.getName());
            categoryRepository.delete(category);
            return "Deleted";
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return "Error";
        }

    }
}
