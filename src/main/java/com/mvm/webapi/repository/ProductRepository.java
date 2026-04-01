package com.mvm.webapi.repository;

import com.mvm.webapi.model.Product;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    private final Map<Long, Product> products = new HashMap<>();
    private long nextId = 1;

    public ProductRepository() {
        // Seed 5 apple products
        save(new Product(null, "Apple iPhone 14", 999.0, "Black"));
        save(new Product(null, "Apple iPhone 13", 799.0, "White"));
        save(new Product(null, "Apple MacBook Pro", 1999.0, "Silver"));
        save(new Product(null, "Apple iPad Air", 599.0, "Blue"));
        save(new Product(null, "Apple Watch Series 8", 399.0, "Red"));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(nextId++);
        }
        products.put(product.getId(), product);
        return product;
    }

    public void deleteById(Long id) {
        products.remove(id);
    }
}