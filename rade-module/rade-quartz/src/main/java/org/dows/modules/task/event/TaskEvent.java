package org.dows.modules.task.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.modules.task.service.TaskInfoService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 事件监听
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEvent implements ApplicationRunner {

    final private TaskInfoService taskInfoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        taskInfoService.init();
        log.info("初始化任务");
    }
}
