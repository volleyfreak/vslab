package com.example.productmanagement.repository;

import com.example.productmanagement.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
