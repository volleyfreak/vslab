package com.example.productmanagement.controller;

import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Optional;
import java.util.Arrays;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.model.ProductObject;


import com.example.productmanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

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
        newProduct.setCategoryId(category.getId());
        Product prod =  productRepository.save(newProduct);
        return new ResponseEntity<>(prod, HttpStatus.OK);
    }

    @GetMapping(path = "products")
    public @ResponseBody Iterable<ProductObject> getProducts() {
        Iterable<Product> products = productRepository.findAll();
        return getFullProducts(products);
    }
    @GetMapping(path="products/{id}")
    public @ResponseBody ProductObject getProductById(@PathVariable int id) {
        Optional<Product> product = productRepository.findById(id);
        Product prod = product.get();
        return getFullProduct(prod);
    }

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

    @GetMapping(path="products/search")
    public @ResponseBody Collection<ProductObject> getProductforSearchValues(String searchDescription, String searchMinPrice, String searchMaxPrice) {
        Iterable<Product> products = productRepository.findAll();
        Iterable<ProductObject> allProducts = getFullProducts(products);
        Stream<ProductObject> stream = StreamSupport.stream(allProducts.spliterator(), false);
        Predicate<ProductObject> minPricePredicate = prod -> prod.getPrice() >= Double.parseDouble(searchMinPrice);
        Predicate<ProductObject> maxPricePredicate = prod -> prod.getPrice() <= Double.parseDouble(searchMaxPrice);
        Predicate<ProductObject> descriptionPredicate = prod -> prod.getName().contains(searchDescription);
        Collection<ProductObject> response = stream.collect(Collectors.toList());
        if (searchMinPrice != null && !searchMinPrice.isEmpty()) {
            response = response.stream().filter(minPricePredicate).collect(Collectors.toList());
        }
        if (searchMaxPrice != null && !searchMaxPrice.isEmpty()) {
            response = response.stream().filter(maxPricePredicate).collect(Collectors.toList());
        }
        if (searchDescription != null && !searchDescription.isEmpty()) {
            response = response.stream().filter(descriptionPredicate).collect(Collectors.toList());
        }
        return response;

    }

//    @GetMapping(path="products/search")
//    public @ResponseBody Iterable<ProductObject> getProductforSearchValues(String searchDescription,
//                                                        Double searchMinPrice, Double searchMaxPrice) {
//        Iterable<Product> products = productRepository.findAll();
//        ArrayList<Product> result = new ArrayList<>();
//        for (Product product: products){
//            if (searchDescription != null){
//                if (product.getName().contains(searchDescription)){
//                    if (result.contains(product)){
//                        continue;
//                    }
//                    else {
//                        result.add(product);
//                    }
//                }
//                else{
//                    continue;
//                }
//            }
//            if (searchMinPrice != null){
//                if (product.getPrice() >= searchMinPrice ){
//                    if (result.contains(product)) {
//                        continue;
//                    }
//                    else {
//                        result.add(product);
//                    }
//                }
//                else {
//                    continue;
//                }
//            }
//            if (searchMaxPrice != null){
//                if (product.getPrice() <= searchMaxPrice ){
//                    if (result.contains(product)) {
//                        continue;
//                    }
//                    else {
//                        result.add(product);
//                    }
//                }
//                else {
//                    continue;
//                }
//            }
//
//        }
//        return getFullProducts(result);
//    }


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

    private ProductObject getFullProduct(Product product) {

        ProductObject fullProduct;
        int categoryId = product.getCategoryId();

        final String uri = "http://category:8081//categories/";
        Category category;

        RestTemplate restTemplate = new RestTemplate();
        category = restTemplate.getForObject(uri + categoryId, Category.class);
        fullProduct = new ProductObject(product.getId(), product.getName(), product.getPrice(), category, product.getDetails(), category.getName());
        return fullProduct;

    }

    private Collection<ProductObject> getFullProducts(Iterable<Product> products) {
        Collection<ProductObject> fullProducts = new ArrayList<>();

        final String uri = "http://category:8081//categories/";

        RestTemplate restTemplate = new RestTemplate();
        Category[] categories;

        try {
            categories = restTemplate.getForObject(uri, Category[].class);
        } catch (Exception e) {
            throw e;
        }

        List<Category> categories_list = Arrays.asList(categories);

        products.forEach(product -> {
            Predicate<Category> categoryIdPredicate = cat -> cat.getId() == product.getCategoryId();
            try {
                fullProducts.add(new ProductObject(product.getId(), product.getName(), product.getPrice(), categories_list.stream().filter(categoryIdPredicate).findFirst().get(), product.getDetails(), categories_list.stream().filter(categoryIdPredicate).findFirst().get().getName()));
            } catch (Exception e) {
            }
        });

        return fullProducts;

    }
}
