package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.microservicemodels.Category;
import hska.iwi.eShopMaster.model.microservicemodels.Product;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductMicroserviceAdapter implements ProductManager {
    public List<Product> getProducts() {
        Product[] productArray = new RestTemplate().getForObject("http://product:8082/product", Product[].class);
        return new ArrayList<Product>(Arrays.asList(productArray));
    }

    public Product getProductById(int id) {
        return new RestTemplate().getForObject("http://product:8082/product/{id}", Product.class, id);
    }

    public Product getProductByName(String name) {
        return new RestTemplate().getForObject("http://product:8082/product/{name}", Product.class, name);
    }

    public int addProduct(String name, double price, int categoryId, String details) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setCategoryId(categoryId);
        product.setDetails(details);
        Product result = new RestTemplate().postForObject("http://product:8082/products", product, Product.class);
        return result.getId();
    }

    public List<Product> getProductsForSearchValues(String searchValue, Double searchMinPrice, Double searchMaxPrice) {
        if (searchMaxPrice == null)
            searchMaxPrice = Double.MAX_VALUE;
        if (searchMinPrice == null)
            searchMinPrice = Double.MIN_VALUE;
        if (searchValue == null)
            searchValue = "";
        Product[] productArray = new RestTemplate().getForObject("http://product:8082/product/search?name=" + searchValue + "&min=" + Double.toString(searchMinPrice) + "&max=" + Double.toString(searchMaxPrice), Product[].class);
        return new ArrayList<Product>(Arrays.asList(productArray));
    }

    public boolean deleteProductsByCategoryId(int categoryId) {
        new RestTemplate().delete("http://product:8082/categories/{id}", categoryId);
        return true;
    }

    public void deleteProductById(int id) {
        new RestTemplate().delete("http://product:8082/categories/{id}", id);
    }
}
