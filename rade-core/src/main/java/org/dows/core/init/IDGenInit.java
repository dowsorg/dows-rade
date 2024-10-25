package org.dows.core.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.leaf.IDGenService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 唯一ID 组件初始化
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class IDGenInit implements ApplicationRunner {

    final private IDGenService idGenService;

    @Override
    public void run(ApplicationArguments args) {
        idGenService.init();
    }
}
