package com.java.mod.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用于存储 Mod 相关的信息
 */
@Data
public class Mod {
    @NotBlank(message = "modId must not be null.")
    @Size(max = 20, message = "modId cannot exceed 20 characters.")
    private String modId;//mod的唯一标识符

    @DecimalMin(value = "0.00", inclusive = true, message = "modVersion must be greater than or equal to 0.00.")
    @DecimalMax(value = "999.99", inclusive = true, message = "modVersion must be less than or equal to 999.99.")
    private Double modVersion;//mod的版本

    private Boolean modAvailable;//mod状态，是否可用

    @Size(max = 50, message = "modInfo cannot exceed 50 characters.")
    private String modInfo;//mod信息
}
