package com.java.mod.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 存储产品的信息
 */
@Data
public class Product {
    @NotBlank(message = "mod 的唯一标识符不能为空")
    @Size(max = 20, message = "mod 的唯一标识符不能超过20个字符")
    private String modId;

    @NotNull(message = " 产品id不能为空")
    private Integer productid;// 产品 id

    @NotNull(message = "产品的类型不能为空")
    private Integer producttype;// 产品的类型

    @NotBlank(message = "产品的路径不能为空")
    @Size(max = 255, message = "产品的路径不能超过255个字符")
    private String productpath;// 产品的路径
}
