package org.dows.modules.dict.service.impl;

import static org.dows.modules.dict.entity.table.DictInfoEntityTableDef.DICT_INFO_ENTITY;
import static org.dows.modules.dict.entity.table.DictTypeEntityTableDef.DICT_TYPE_ENTITY;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.dict.entity.DictTypeEntity;
import org.dows.modules.dict.mapper.DictInfoMapper;
import org.dows.modules.dict.mapper.DictTypeMapper;
import org.dows.modules.dict.service.DictTypeService;
import com.mybatisflex.core.query.QueryWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 字典类型
 */
@Service
@RequiredArgsConstructor
public class DictTypeServiceImpl extends BaseServiceImpl<DictTypeMapper, DictTypeEntity> implements
    DictTypeService {

    final private DictInfoMapper dictInfoMapper;

    @Override
    public List<DictTypeEntity> list(QueryWrapper queryWrapper) {
        return super.list(
            queryWrapper.select(DICT_TYPE_ENTITY.ID, DICT_TYPE_ENTITY.KEY,
                DICT_TYPE_ENTITY.NAME));
    }

    @Override
    public boolean delete(Long... ids) {
        super.delete(ids);
        return dictInfoMapper.deleteByQuery(
            QueryWrapper.create().and(DICT_INFO_ENTITY.TYPE_ID.in((Object) ids))) > 0;
    }
}