package org.dows.modules.space.service.impl;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.space.entity.SpaceInfoEntity;
import org.dows.modules.space.mapper.SpaceInfoMapper;
import org.dows.modules.space.service.SpaceInfoService;
import org.springframework.stereotype.Service;

/**
 * 文件空间信息
 */
@Service
public class SpaceInfoServiceImpl extends BaseServiceImpl<SpaceInfoMapper, SpaceInfoEntity>
        implements SpaceInfoService {
}