package org.dows.core.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.plugin.service.RadePluginService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 历史安装过的插件执行初始化
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RadePluginInit implements ApplicationRunner {

    final private RadePluginService radePluginService;

    @Override
    public void run(ApplicationArguments args) {
        radePluginService.init();
    }
}
