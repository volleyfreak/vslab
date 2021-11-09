package com.example.productmanagement.controller;

import com.example.productmanagement.model.Product;
import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Check this to do the real implementation https://spring.io/guides/tutorials/rest/
@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @PostMapping(path="product") // Map ONLY POST Requests
    public @ResponseBody String addNewProduct (@RequestBody Product product) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        productRepository.save(product);
        return "Saved";
    }

    @GetMapping(path="product")
    public @ResponseBody Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping(path="productsearch")
    public @ResponseBody Iterable<Product> getProductforSearchValues(String searchDescription,
                                                                     Double searchMinPrice, Double searchMaxPrice) {
        Iterable<Product> products = productRepository.findAll();
        //todo: filter
        return products;
    }

    @DeleteMapping(path="product")
    public @ResponseBody String getAllCategories(@RequestBody Product product) {
        productRepository.delete(product);
        return "Deleted";
    }
}
