package org.dows.modules.task.service.impl;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.task.entity.TaskLogEntity;
import org.dows.modules.task.mapper.TaskLogMapper;
import org.dows.modules.task.service.TaskInfoLogService;
import org.springframework.stereotype.Service;

@Service
public class TaskInfoLogServiceImpl extends BaseServiceImpl<TaskLogMapper, TaskLogEntity>
        implements TaskInfoLogService {
}
