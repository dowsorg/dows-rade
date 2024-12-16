package org.dows.sbi.config;

import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.engine.freemarker.FreemarkerEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:31 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Configuration
public class SbiConfiguration {

    @Bean
    public TemplateEngine classpathTemplateEngine() {
        /*Configuration cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_32);
        //cfg.setDirectoryForTemplateLoading(new File(rootPath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);*/
        TemplateConfig templateConfig = new TemplateConfig("ftl", TemplateConfig.ResourceMode.CLASSPATH);
        return new FreemarkerEngine(templateConfig);
    }


    /*@Bean
    public TemplateEngine filepathTemplateEngine() {
        *//*Configuration cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_32);
        //cfg.setDirectoryForTemplateLoading(new File(rootPath));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);*//*
        TemplateConfig templateConfig = new TemplateConfig("ftl", TemplateConfig.ResourceMode.FILE);
        return new FreemarkerEngine(templateConfig);
    }*/

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        int corePoolSize = 5;
        int maximumPoolSize = 10;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(100);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue
        );
        return threadPoolExecutor;
    }

}

