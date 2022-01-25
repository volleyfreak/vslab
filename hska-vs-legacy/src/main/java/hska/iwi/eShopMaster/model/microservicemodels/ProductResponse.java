package hska.iwi.eShopMaster.model.microservicemodels;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class ProductResponse {
        public ProductResponse( Product prod) {
            this.id = prod.getId();
            this.name = prod.getName();
            this.price = prod.getPrice();
            this.categoryId = prod.getCategoryId();
            this.details = prod.getDetails();
            try {
                CategoryResponse category = new RestTemplate().getForObject("http://category:8080/categories/{id}", CategoryResponse.class, this.categoryId);
                this.categoryName = category.getName();
            } catch(Exception ex) {
                this.categoryName = "Unknonwn category";
            }
        }


        private Integer id;
    
        private String name;
    
        private double price;
    
        private Integer categoryId;
    
        private String details;

        private String categoryName;

        public Integer getId() {
            return id;
        }
    
        public String getName() {
            return name;
        }
    
        public double getPrice() {
            return price;
        }
    
        public Integer getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }
    
        public String getDetails() {
            return details;
        }

        public static List<ProductResponse> getList (Iterable<Product> products) {
            List<ProductResponse> list = new ArrayList<ProductResponse>();
            for(Product product: products){
                list.add(new ProductResponse(product));
            }

            return list;
        }
}
