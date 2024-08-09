package com.java.mod.controller;

import com.java.mod.apiResponse.ApiResponse;
import com.java.mod.dto.Mod;
import com.java.mod.service.ModService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mods")
@Slf4j
public class ModController {
    /**
     * Mod服务接口的实例，用于执行业务逻辑
     */
    @Autowired
    private ModService modService;

    /**
     * 根据id获取模组的GET请求处理器
     *
     * @param modId 模组id
     * @return 如果找到模组，返回200 OK状态码和包含模组对象的响应体；
     *         如果模组不存在，则返回404 NOT_FOUND状态码和错误信息
     */
    @GetMapping("/{modId}")
    public ResponseEntity<ApiResponse<Mod>> getMod(@PathVariable String modId) {
        log.info("Received GET request for mod with id: {}", modId);

        if (modService.getModByModId(modId) == null){
            log.warn("Attempted to retrieve non-existent mod with id: {}", modId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(404, "Mod not found", null));
        }
        Mod mod = modService.getModByModId(modId);
        log.info("Retrieved mod with id: {}", modId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", mod));
    }

    /**
     * 获取所有模组的GET请求处理器
     *
     * @return 包含所有模组的列表
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Mod>>> getModAll(){
        log.info("Received GET request for all mods");

        List<Mod> modList = modService.getModAll();
        log.info("Found all mods");
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", modList));
    }
}
