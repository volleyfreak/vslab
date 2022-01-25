package hska.iwi.eShopMaster.model.microservicemodels;

import java.io.Serializable;

public class Category implements Serializable{
    private Integer id;

    private String name;

    // This is probably omitted since category and product are separate
    // microservices which use different databases
    //private Set<Product> products = new HashSet<Product>(0);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
