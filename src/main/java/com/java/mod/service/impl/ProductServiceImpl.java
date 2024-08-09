package com.java.mod.service.impl;

import com.java.mod.dto.Game;
import com.java.mod.dto.Product;
import com.java.mod.mapper.GameMapper;
import com.java.mod.mapper.ModMapper;
import com.java.mod.mapper.ProductMapper;
import com.java.mod.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ModMapper modMapper;

    @Autowired
    private GameMapper gameMapper;


    @Override
    public Product insertProduct(Product product) {
        if (modMapper.getModByModId(product.getModId()) == null) {
            return null;
        }
        int i = productMapper.insertProduct(product);
        if (i <= 0) {
            return null;
        }
        return product;
    }

    @Override
    public Product getProductByProductId(Integer productid) {
        return productMapper.getProductByProductId(productid);
    }

    @Override
    public List<Product> getProductByModid(String modId) {
        return productMapper.getProductByModid(modId);
    }

    @Override
    public Product updateProduct(Integer productid, Product product) {
        Product updated = productMapper.getProductByProductId(productid);
        if (updated == null){
            return null;
        }
        if (modMapper.getModByModId(product.getModId()) == null) {
            return null;
        }
        updated.setModId(product.getModId());
        updated.setProductid(product.getProductid());
        updated.setProducttype(product.getProducttype());
        updated.setProductpath(product.getProductpath());
        productMapper.updateProduct(updated);
        return updated;
    }

    @Override
    public Boolean deleteProduct(Integer productid) {
        List<Game> games = gameMapper.getGameByModidAndProductid(productid);
        for (Game game : games) {
            gameMapper.deleteGame(game.getGamename());
        }
        if (productMapper.deleteProduct(productid) > 0){
            return true;
        }
        return false;
    }


    @Override
    public List<Product> getAllProducts() {
        return productMapper.getAllProducts();
    }
}
