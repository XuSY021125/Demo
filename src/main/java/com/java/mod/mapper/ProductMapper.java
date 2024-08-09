package com.java.mod.mapper;

import com.java.mod.dto.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Insert("INSERT INTO table_products (modId, productid, producttype, productpath) VALUES (#{modId}, #{productid}, #{producttype}, #{productpath})")
    int insertProduct(Product product);

    @Select("SELECT * FROM table_products WHERE productid = #{productid}")
    Product getProductByProductId(Integer productid);

    @Select("SELECT * FROM table_products WHERE modId = #{modId}")
    List<Product> getProductByModid(String modId);

    @Update("UPDATE table_products SET modId = #{modId}, producttype = #{producttype}, productpath = #{productpath} WHERE productid = #{productid}")
    int updateProduct(Product product);

    @Delete("DELETE FROM table_products WHERE productid = #{productid}")
    int deleteProduct(Integer productid);

    @Select("SELECT * FROM table_products")
    List<Product> getAllProducts();
}