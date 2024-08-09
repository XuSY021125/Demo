package com.java.mod.service;

import com.java.mod.dto.Mod;
import com.java.mod.entity.Page;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ModService {
    Boolean create(Mod mod);
    Boolean delete(String modId);
    Boolean deleteCompletely(String modId);
    Mod update(Mod mod);
    Mod getModByModId(String modId);
    List<Mod> getModAll();
    Page<Mod> getModsPaged(RowBounds rowBounds);
}
