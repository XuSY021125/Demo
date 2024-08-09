package com.java.mod.service;

import com.java.mod.dto.Product;

import java.util.List;

public interface ProductService {

    Product insertProduct(Product product);

    Product updateProduct(Integer productid, Product product);


    Boolean deleteProduct(Integer productid);

    Product getProductByProductId(Integer productid);

    List<Product> getProductByModid(String modId);

    List<Product> getAllProducts();
}
