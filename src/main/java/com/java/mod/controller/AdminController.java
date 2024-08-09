package com.java.mod.controller;

import com.java.mod.annotation.Authentication;
import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.dto.Mod;
import com.java.mod.dto.Product;
import com.java.mod.dto.User;
import com.java.mod.entity.Page;
import com.java.mod.enums.Role;
import com.java.mod.service.ModService;
import com.java.mod.service.ProductService;
import com.java.mod.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController类用于处理与用户管理相关的HTTP请求
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    /**
     * 用户服务接口的实例，用于执行业务逻辑
     */
    @Autowired
    private UserService userService;

    /**
     * Mod服务接口的实例，用于执行业务逻辑
     */
    @Autowired
    private ModService modService;

    @Autowired
    private ProductService productService;

    /**
     * 创建新模组的POST请求处理器
     *
     * @param mod 要创建的模组对象
     * @return 如果创建成功，返回201 CREATED状态码和包含模组对象的响应体；
     *         如果模组已存在，则返回409 CONFLICT状态码和错误信息；
     *         如果创建失败，则返回400 BAD_REQUEST状态码和错误信息
     */
    @PostMapping("/mods")
    @Authentication
    public ResponseEntity<ApiResponse<Mod>> create(@RequestBody @Valid Mod mod) {
        log.info("Received POST request to create a mod with id: {}", mod.getModId());
        if (modService.getModByModId(mod.getModId()) != null){
            log.error("Attempted to create an existing mod with id: {}", mod.getModId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(409, "Mod already exists", null));
        }
        if (modService.create(mod)){
            log.info("Successfully created mod with id: {}", mod.getModId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "Mod created successfully", mod));
        }
        log.error("Failed to create mod with id: {}", mod.getModId());
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Failed to create mod", null));
    }

    /**
     * 删除指定id模组的DELETE请求处理器, 此请求不会删除数据库内容
     *
     * @param modId 要删除的模组id
     * @return 如果删除成功，返回200 OK状态码和确认信息；
     *         如果模组不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @DeleteMapping("/mods/{modId}")
    @Authentication
    public ResponseEntity<ApiResponse<Mod>> delete(@PathVariable String modId) {
        log.info("Received DELETE request for mod with id: {}", modId);
//        List<Product> products = productService.getProductByModid(modId);
//        for (Product product : products) {
//            productService.deleteProduct(product.getProductid());
//        }
        if (modService.delete(modId)){
            log.info("Successfully deleted mod with id: {}", modId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Delete successfully" , null ));
        }
        log.error("Failed to delete mod with id: {}", modId);
        return ResponseEntity.status(404).body(new ApiResponse<>(404, "Mod not found, delete failed", null));
    }

    /**
     * 删除指定id模组的DELETE请求处理器, 此请求会完全删除数据库的数据
     *
     * @param modId 要删除的模组id
     * @return 如果删除成功，返回200 OK状态码和确认信息；
     *         如果模组不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @DeleteMapping("/mods/deleteCompletely/{modId}")
    public ResponseEntity<ApiResponse<Mod>> deleteCompletely(@PathVariable String modId){
        log.info("Received DELETE request for mod with id: {}", modId);

        if (modService.deleteCompletely(modId)){
            log.info("Success. The mod has been completely removed, mod with id: {}", modId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Completely deleted successfully" , null ));
        }
        log.error("Failed to completely delete mod with id: {}", modId);
        return ResponseEntity.status(404).body(new ApiResponse<>(404, "Mod not found, delete Completely failed", null));
    }
    /**
     * 更新指定id模组的PUT请求处理器
     *
     * @param modId 要更新的模组id
     * @param mod 包含更新信息的模组对象
     * @return 如果更新成功，返回200 OK状态码和包含更新后模组对象的响应体；
     *         如果模组不存在，则返回404 NOT_FOUND状态码和错误信息；
     *         如果更新失败，则返回400 BAD_REQUEST状态码和错误信息
     */
    @PutMapping("/mods/{modId}")
    public ResponseEntity<ApiResponse<Mod>> update(@PathVariable String modId, @RequestBody @Valid Mod mod) {
        log.info("Received PUT request to update mod with id: {}", modId);
        Mod beforeUp = modService.getModByModId(modId);
        Mod updated = modService.update(mod);

        if (updated == null){
            log.warn("Attempted to update non-existent mod with id: {}", modId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Mod not found, update failed", null));
        }
        if (beforeUp.equals(updated)){
            log.error("Failed to update mod with id: {}", modId);
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Update failed. The updated mod already exists", updated));
        }
        log.info("Successfully updated mod with id: {}", modId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Update successful", mod));
    }

    /**
     * 根据id获取模组的GET请求处理器
     *
     * @param modId 模组id
     * @return 如果找到模组，返回200 OK状态码和包含模组对象的响应体；
     *         如果模组不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @GetMapping("/mods/{modId}")
    public ResponseEntity<ApiResponse<Mod>> getMod(@PathVariable String modId) {
        log.info("Received GET request for mod with id: {}", modId);

        if (modService.getModByModId(modId) == null){
            log.error("Attempted to query non-existent mod with id: {}", modId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Mod not found", null));
        }
        Mod mod = modService.getModByModId(modId);
        log.info("Successfully query mod with id: {}", modId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", mod));
    }

    /**
     * 处理获取所有模组的分页 GET 请求。
     *
     * @param page  请求的页码（从1开始）
     * @param size  每页的大小
     * @return 包含当前页所有模组的分页响应
     */
    @GetMapping("/mods/page/{page}")
    public ResponseEntity<ApiResponse<Page<Mod>>> getModsPaged(@PathVariable int page, @RequestParam(defaultValue = "5") int size) {
        log.info("Received GET request for all mods with pagination: page {}, size {}", page, size);

        // 使用 RowBounds 进行分页查询
        int offset = (page - 1) * size; // 计算偏移量，注意：页码从1开始
        RowBounds rowBounds = new RowBounds(offset, size);

        Page<Mod> modPage = modService.getModsPaged(rowBounds);
        if (modPage.getContent() == null){

        }
        log.info("Found mods with pagination");
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", modPage));
    }

    /**
     * 创建新用户的POST请求处理器
     *
     * @param user 要创建的用户对象
     * @return 如果用户创建成功，则返回201 CREATED状态码和新创建的用户对象；
     *         如果创建失败，则返回400 BAD_REQUEST状态码和错误信息
     */
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.insertUser(user);
        if (createdUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(201, "User created successfully", createdUser));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Failed to create user", null));
    }

    /**
     * 删除指定userId用户的DELETE请求处理器
     *
     * @param userId 用户userId
     * @return 如果用户成功删除，则返回200 OK状态码和成功消息；
     *         如果用户不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @DeleteMapping("user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.status(404).body("User not found");
    }

    /**
     * 更新指定userId用户的PUT请求处理器
     *
     * @param userId 用户userId
     * @param user 包含更新信息的用户对象
     * @return 如果用户更新成功，则返回200 OK状态码和更新后的用户对象；
     *         如果用户不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @PutMapping("/user/userId/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUserByUserId(@PathVariable("userId") String userId, @RequestBody @Valid User user) {
        user.setUserId(userId);
        User updated = userService.updateUser(userId, user);

        return ResponseEntity.ok(new ApiResponse<>(200,"User updated successfully", updated));

    }

    /**
     * 将指定用户名的用户设为管理员的PUT请求处理器
     *
     * @param username 用户名
     * @return 如果用户更新成功，则返回200 OK状态码和成功消息；
     *         如果用户不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @PutMapping("/user/{username}/beAdmin")
    public ResponseEntity<ApiResponse<User>> beAdmin(@PathVariable("username") String username){
        User user = userService.getUserByUsername(username);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "User not found", null));
        }
        user.setRole(Role.ADMIN);
        userService.updateUerRole(user);
        return ResponseEntity.ok(new ApiResponse<>(200, user.getUsername() + " has become an ADMIN", user));
    }

    /**
     * 将指定用户名的用户设为普通用户的PUT请求处理器。
     *
     * @param username 用户名
     * @return 如果用户更新成功，则返回200 OK状态码和成功消息；
     *         如果用户不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @PutMapping("/user/{username}/beUser")
    public ResponseEntity<ApiResponse<User>> beUser(@PathVariable("username") String username){
        User user = userService.getUserByUsername(username);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "User not found", null));

        }
        user.setRole(Role.USER);
        userService.updateUerRole(user);
        return ResponseEntity.ok(new ApiResponse<>(200, user.getUsername() + " has become an USER", user));
    }


    /**
     * 获取所有用户的GET请求处理器
     *
     * @return 包含所有用户的列表
     */
    @GetMapping("/user/all")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", userList));
    }

    /**
     * 根据userId获取用户的GET请求处理器
     *
     * @param userId 用户userId
     * @return 如果找到用户，则返回200 OK状态码和用户对象；
     *         如果用户不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @GetMapping("/user/userId/{userId}")
    public ResponseEntity<ApiResponse<User>> getUserByUserId(@PathVariable("userId") String userId) {
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "User not found", null));

        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
    }

    /**
     * 根据username获取用户的GET请求处理器
     *
     * @param username 用户username
     * @return 如果找到用户，则返回200 OK状态码和用户对象；
     *         如果用户不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @GetMapping("/user/username/{username}")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "User not found", null));

        }
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", user));
    }
}