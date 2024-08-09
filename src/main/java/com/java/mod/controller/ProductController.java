package com.java.mod.controller;

import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.dto.Game;
import com.java.mod.dto.Product;
import com.java.mod.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController 类用于处理与产品相关的 HTTP 请求。
 */
@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 创建新产品的 POST 请求处理器
     *
     * @param product 要创建的产品对象
     * @return 如果创建成功，返回 201 CREATED 状态码和包含产品对象的响应体；
     *         如果产品已存在，则返回 409 CONFLICT 状态码和错误信息；
     *         如果创建失败，则返回 400 BAD_REQUEST 状态码和错误信息。
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody @Valid Product product) {
        if (productService.getProductByProductId(product.getProductid()) != null){
            log.warn("Attempted to create an existing pruduct with productid: {}", product.getProductid());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, "Product already exists", null));
        }
        if (productService.insertProduct(product) == null){
            log.error("Failed to create pruduct with productid: {}",  product.getProductid());
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Failed to create product", null));
        }
        log.info("Successfully created pruduct withproductid: {}", product.getProductid());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Product created successfully", product));
    }

    /**
     * 根据模组ID和产品ID获取产品的 GET 请求处理器
     *
     * @param productid  产品ID
     * @return 如果找到产品，返回 200 OK 和产品对象；
     *         如果未找到产品，则返回 404 NOT_FOUND 和错误信息
     */
    @GetMapping("/{productid}")
    public ResponseEntity<ApiResponse<Product>> getProduct(@PathVariable Integer productid) {
        Product product = productService.getProductByProductId(productid);
        if (product == null) {
            log.warn("Attempted to retrieve non-existent pruduct with productid: {}", productid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Product not found", null));

        }
        log.info("Query pruduct with productid: {}",  productid);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", product));
    }

    /**
     * 更新产品的 PUT 请求处理器
     *
     * @param productid  产品ID
     * @param product    包含更新信息的产品对象
     * @return 200 OK 和更新后的产品对象
     */
    @PutMapping("/{productid}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Integer productid, @RequestBody  Product product) {
        product.setProductid(productid);
        Product beforeUp = productService.getProductByProductId(productid);
        Product updated = productService.updateProduct(productid, product);
        if (updated == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Update failed. Product or mod not found", null));
        }
        if (beforeUp.equals(product)){
            return ResponseEntity.badRequest().body(new ApiResponse<>(400,"Product updated failed. The updated product already exists", updated));
        }
        return ResponseEntity.ok(new ApiResponse<>(200,"Product updated successfully", updated));
    }

    /**
     * 删除产品的 DELETE 请求处理器
     *
     * @param productid  产品ID
     * @return 如果删除成功，返回 200 OK 和确认消息；
     *         如果未找到产品，则返回 404 NOT_FOUND 和错误信息
     */
    @DeleteMapping("/{productid}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productid) {
        boolean deleted = productService.deleteProduct(productid);
        if (deleted) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.status(404).body("Product not found");
    }

    /**
     * 获取所有产品的 GET 请求处理器
     *
     * @return 200 OK 和包含所有产品列表的响应体
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", productList));
    }
}
