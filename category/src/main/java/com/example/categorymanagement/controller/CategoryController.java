package com.example.categorymanagement.controller;



import com.example.categorymanagement.model.Category;
import com.example.categorymanagement.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

// Check this to do the real implementation https://spring.io/guides/tutorials/rest/
@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping(path="/categories") // Map ONLY POST Requests
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Category addNewCategory (@RequestBody Category newCategory) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        return categoryRepository.save(newCategory);
    }

    @GetMapping(path="/categories")
    public @ResponseBody Iterable<Category> getAllCategories() {
        // This returns a JSON or XML with the users
        return categoryRepository.findAll();
    }

    @DeleteMapping(path="/categories/{id}")
    public void deleteCategory(@PathVariable int id) {
        try {
            //new RestTemplate().delete("http://product:8082//categories/{id}", id);

        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }
        categoryRepository.deleteById(id);
    }
}
