package com.java.mod.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 存储游戏的信息
 */
@Data
public class Game {
    @NotBlank(message = "游戏名称不能为空")
    @Size(max = 50, message = "游戏名称不能超过50个字符")
    private String gamename;// 游戏名称

    @NotBlank(message = "mod 的唯一标识符不能为空")
    @Size(max = 20, message = "mod 的唯一标识符不能超过20个字符")
    private String modId;// mod 的唯一标识符

    @NotNull(message = " 产品id不能为空")
    private Integer productid;// 产品 id
}

