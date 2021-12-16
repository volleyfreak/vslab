package com.example.productmanagement.controller;

import java.net.InetAddress;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.example.productmanagement.model.Product;
import com.example.productmanagement.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;

// Check this to do the real implementation https://spring.io/guides/tutorials/rest/
@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    Environment environment;

    @PostMapping(path = "product") // Map ONLY POST Requests
    public @ResponseBody
    String addNewProduct(@RequestBody Product product) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        productRepository.save(product);
        return "Saved";
    }

    @GetMapping(path = "product")
    public @ResponseBody
    Iterable<Product> getProducts() {
        return productRepository.findAll();
    }
    //todo: getmapping auf selben endpoint


    @GetMapping(path="ip")
    public @ResponseBody String getIp() {
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e){
            return "localhost not available";
        }

    }

    @GetMapping(path="productsearch")
    public @ResponseBody Iterable<Product> getProductforSearchValues(String searchDescription,
                                                                     Double searchMinPrice, Double searchMaxPrice) {
        Iterable<Product> products = productRepository.findAll();
        ArrayList<Product> result = new ArrayList<>();
        for (Product product: products){
            if (searchDescription != null){
                if (product.getName().contains(searchDescription)){
                    result.add(product);
                }
                else{
                    continue;
                }
            }
            if (searchMinPrice != null){
                if (product.getPrice() >= searchMinPrice ){
                    result.add(product);
                }
                else {
                    continue;
                }
            }
            if (searchMaxPrice != null){
                if (product.getPrice() <= searchMaxPrice ){
                    result.add(product);
                }
                else {
                    continue;
                }
            }

        }
        return result;
    }

    @DeleteMapping(path="product")
    public @ResponseBody String DeleteProduct(@RequestBody Product product) {
        productRepository.delete(product);
        return "Deleted";
    }

    @DeleteMapping(path="category/{id}")
    public @ResponseBody ResponseEntity<Integer> deleteProductsByCategoryId(@PathVariable int id) {
        Iterable<Product> products = productRepository.findAll();
        Stream<Product> stream = StreamSupport.stream(products.spliterator(), false);
        Predicate<Product> categoryPredicate = prod -> prod.getCategoryID() == id;
        Collection<Product> productList = stream.filter(categoryPredicate).collect(Collectors.toList());

        try {
            productRepository.deleteAll(productList);
        } catch(Exception e) {
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
