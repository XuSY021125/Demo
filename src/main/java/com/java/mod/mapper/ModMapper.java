package com.java.mod.mapper;

import com.java.mod.dto.Mod;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ModMapper {
    @Insert("INSERT INTO table_mods (mod_id, mod_version, mod_available, mod_info) VALUES (#{modId}, #{modVersion}, #{modAvailable}, #{modInfo})")
    int create(Mod mod);

    @Update("UPDATE table_mods SET mod_version=#{modVersion}, mod_available=#{modAvailable}, mod_info=#{modInfo} WHERE mod_id=#{modId}")
    int update(Mod mod);

    @Update("UPDATE table_mods SET mod_exist = 1 WHERE mod_id=#{modId}")
    int restore(String modId);

    @Delete("UPDATE table_mods SET mod_exist = 0 WHERE mod_id=#{modId}")
    int delete(String modId);

    @Delete("DELETE FROM table_mods WHERE mod_id=#{modid};")
    int deleteCompletely(String modId);

    @Select("SELECT * FROM table_mods WHERE mod_id=#{modId}")
    @Results(id = "getMod", value = {
            @Result(property = "modId", column = "mod_id"),
            @Result(property = "modVersion", column = "mod_version"),
            @Result(property = "modAvailable", column = "mod_available"),
            @Result(property = "modInfo", column = "mod_info")
    })
    Mod getMod(String modId);
    @Select("SELECT * FROM table_mods WHERE mod_id=#{modId} and mod_exist = 0")
    @Results(id = "getModById", value = {
            @Result(property = "modId", column = "mod_id"),
            @Result(property = "modVersion", column = "mod_version"),
            @Result(property = "modAvailable", column = "mod_available"),
            @Result(property = "modInfo", column = "mod_info")
    })
    Mod getModIsDeletedMod(String modId);
    
    @Select("SELECT * FROM table_mods WHERE mod_id=#{modId} and mod_exist != 0")
    @Results(id = "getModByModId", value = {
            @Result(property = "modId", column = "mod_id"),
            @Result(property = "modVersion", column = "mod_version"),
            @Result(property = "modAvailable", column = "mod_available"),
            @Result(property = "modInfo", column = "mod_info")
    })
    Mod getModByModId(String modId);

    @Select("SELECT * FROM table_mods where mod_exist != 0")
    @Results(id = "getModAll", value = {
            @Result(property = "modId", column = "mod_id"),
            @Result(property = "modVersion", column = "mod_version"),
            @Result(property = "modAvailable", column = "mod_available"),
            @Result(property = "modInfo", column = "mod_info")
    })
    List<Mod> getModAll();

    @Select("SELECT * FROM table_mods where mod_exist != 0 LIMIT #{offset}, #{limit}")
    @Results(id = "getModsPaged", value = {
            @Result(property = "modId", column = "mod_id"),
            @Result(property = "modVersion", column = "mod_version"),
            @Result(property = "modAvailable", column = "mod_available"),
            @Result(property = "modInfo", column = "mod_info")
    })
    List<Mod> getModsPaged(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM table_mods where mod_exist != 0")
    long getTotalModsCount();
}