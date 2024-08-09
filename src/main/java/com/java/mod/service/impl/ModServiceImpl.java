package com.java.mod.service.impl;

import com.java.mod.dto.Mod;
import com.java.mod.entity.Page;
import com.java.mod.mapper.ModMapper;
import com.java.mod.mapper.ProductMapper;
import com.java.mod.service.ModService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Mod服务实现类，提供对Mod的操作方法
 */
@Service
public class ModServiceImpl implements ModService {
    @Autowired
    private  ModMapper modMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 创建一个新的Mod记录
     *
     * @param mod 要创建的Mod对象
     * @return 如果创建成功返回true，否则返回false
     */
    public Boolean create(Mod mod) {
        if (modMapper.getModIsDeletedMod(mod.getModId()) != null){
            modMapper.restore(mod.getModId());
            return true;
        }
        return modMapper.create(mod) > 0;
    }

    /**
     * 删除指定ID的Mod记录
     *
     * @param modId 要删除的Mod的ID
     * @return 如果删除成功返回true，否则返回false
     */
    public Boolean delete(String modId) {
        if (modMapper.getModByModId(modId) == null){
            return false;
        }
        modMapper.delete(modId);
        return true;
    }

    public Boolean deleteCompletely(String modId) {
        if (modMapper.getMod(modId) != null) {
            modMapper.deleteCompletely(modId);
            return true;
        }
        return false;
    }

    /**
     * 更新Mod记录
     *
     * @param mod 要更新的Mod对象
     * @return 如果更新成功返回true，否则返回false
     */
    public Mod update(Mod mod) {
        Mod updated = modMapper.getModByModId(mod.getModId());
        if (updated == null) {
            return null;
        }
        updated.setModId(mod.getModId());
        updated.setModVersion(mod.getModVersion());
        updated.setModAvailable(mod.getModAvailable());
        updated.setModInfo(mod.getModInfo());
        modMapper.update(updated);
        return updated;
    }

    /**
     * 根据ID获取Mod记录
     *
     * @param modId 要获取的Mod的ID
     * @return 查询到的Mod对象，如果没有找到则返回null
     */
    public Mod getModByModId(String modId) {
        return modMapper.getModByModId(modId);
    }

    /**
     * 获取所有Mod记录
     *
     * @return 所有的Mod对象列表
     */
    public List<Mod> getModAll() {
        return modMapper.getModAll();
    }


    /**
     * 分页查询获取所有Mod记录
     *
     * @param rowBounds 分页参数，包含偏移量和每页大小
     * @return 包含分页信息的Mod对象列表
     */
    public Page<Mod> getModsPaged(RowBounds rowBounds) {
        List<Mod> mods = modMapper.getModsPaged(rowBounds.getOffset(), rowBounds.getLimit());
        long total = modMapper.getTotalModsCount();
        // 计算当前页码
        int currentPage = rowBounds.getOffset() / rowBounds.getLimit() + 1; // 注意：加1是因为页码从1开始

        return new Page<>(currentPage, rowBounds.getLimit(), total, mods);
    }
}
