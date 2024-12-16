package org.dows.modules.space.service.impl;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.space.entity.SpaceTypeEntity;
import org.dows.modules.space.mapper.SpaceTypeMapper;
import org.dows.modules.space.service.SpaceTypeService;
import org.springframework.stereotype.Service;

/**
 * 文件空间信息
 */
@Service
public class SpaceTypeServiceImpl extends BaseServiceImpl<SpaceTypeMapper, SpaceTypeEntity>
        implements SpaceTypeService {
}