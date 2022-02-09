package com.example.productmanagement.controller;

import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Product;


import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;

// Check this to do the real implementation https://spring.io/guides/tutorials/rest/
@RestController // This means that this class is a Controller
@RequestMapping(path="/")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    Environment environment;


    @PostMapping(path="products") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity<Product> addNewProduct (@RequestBody Product newProduct) {
        Category category;
        try {
            category = new RestTemplate().getForObject("http://category:8081/categories/{id}", Category.class, newProduct.getCategoryId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(newProduct, HttpStatus.NOT_FOUND);
        }
        newProduct.setCategoryName(category.getName());
        Product prod =  productRepository.save(newProduct);
        return new ResponseEntity<>(prod, HttpStatus.OK);
    }


//    @PostMapping(path = "product") // Map ONLY POST Requests
//    public @ResponseBody
//    String addNewProduct(@RequestBody Product product) {
//        // @ResponseBody means the returned String is the response, not a view name
//        // @RequestParam means it is a parameter from the GET or POST request
//        try {
//            ResponseEntity<Category[]> categories = new RestTemplate().getForEntity("http://category:8081/categories", Category[].class );
//            Category[] categoryIter = categories.getBody();
//            ArrayList<Category> result = new ArrayList<>();
//            for (Category category: categoryIter){
//                    if (product.getName().equals(product.getCategoryName())){
//                        result.add(category);
//                    }
//                }
//            if (!result.isEmpty()){
//                productRepository.save(product);
//                return "Saved";
//            }
//        } catch (RestClientException e) {
//            System.out.println(e.getMessage());
//        }
//        return "Error";
//    }

    @GetMapping(path = "products")
    public @ResponseBody
    Iterable<Product> getProducts() {
        return productRepository.findAll();
    }
    //todo: getmapping auf selben endpoint


    @GetMapping(path="ip")
    public @ResponseBody String getIp() {
        try{
            Iterable<Product> products = productRepository.findAll();
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (Exception e){
            return "localhost not available";
        }

    }
    //todo: Hier kommen irgendwie 3 Produkte zurück??
    @GetMapping(path="products/search")
    public @ResponseBody ResponseEntity<Iterable<Product>> getProductforSearchValues(String searchDescription,
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
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path="products/{id}")
    public @ResponseBody ResponseEntity<Integer> deleteProductById(@PathVariable int id) {
        HttpStatus response;

        try {
            productRepository.deleteById(id);
            response = HttpStatus.OK;
        } catch(Exception e) {
            response = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(id, response);
    }

    @DeleteMapping(path="categories/{id}")
    public @ResponseBody ResponseEntity<Integer> deleteProductsByCategoryId(@PathVariable int id) {
        Iterable<Product> products = productRepository.findAll();
        Stream<Product> stream = StreamSupport.stream(products.spliterator(), false);
        Predicate<Product> categoryPredicate = prod -> prod.getCategoryId() == id;
        Collection<Product> productList = stream.filter(categoryPredicate).collect(Collectors.toList());

        try {
            productRepository.deleteAll(productList);
        } catch(Exception e) {
            return new ResponseEntity<>(id, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
