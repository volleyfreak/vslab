package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.microservicemodels.Category;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryMicroserviceAdapter implements CategoryManager {

    public List<Category> getCategories() {
        Category[] categoryArray = new RestTemplate().getForObject("http://category:8081/categories", Category[].class);
        return new ArrayList<Category>(Arrays.asList(categoryArray));
    }

    public Category getCategory(int id) {
        return new RestTemplate().getForObject("http://category:8081/categories/{id}", Category.class, id);
    }

    public Category getCategoryByName(String name) {
        return new RestTemplate().getForObject("http://category:8081/categories/{name}", Category.class, name);
    }

    public void addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        Category result = new RestTemplate().postForObject("http://category:8081/categories", category, Category.class);
    }

    public void delCategory(Category cat) {
        new RestTemplate().delete("http://category:8081/categories/{id}", cat.getId());
    }

    public void delCategoryById(int id) {
        new RestTemplate().delete("http://category:8081/categories/{id}", id);
    }
}
