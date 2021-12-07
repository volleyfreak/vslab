package com.example.categorymanagement.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.categorymanagement.model.Category;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface CategoryRepository extends CrudRepository<Category, Integer>{
}
