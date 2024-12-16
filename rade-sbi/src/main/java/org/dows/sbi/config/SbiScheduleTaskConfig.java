package org.dows.sbi.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


/**
 * @description: 定时任务配置</ br>
 * @author: lait.zhang@gmail.com
 * @date: 4/1/2024 12:04 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableScheduling
//@PropertySource("classpath:/scan-task.ini")
public class SbiScheduleTaskConfig implements SchedulingConfigurer {


    /**
     * 支持多任务
     *
     * @param taskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        /*taskMap.forEach((key, runnable) -> {
            //动态使用cron表达式设置循环间隔
            taskRegistrar.addTriggerTask(() -> {
                runnable.run();
                log.info("current time: {}, execute: {}", LocalDateTime.now(), runnable.getClass().getSimpleName());
            }, triggerContext -> {
                String cron = taskCronMap.get(key);
                log.info("current cron: {}", cron);
                if (null == cron) {
                    cron = runnable.getCron();
                    taskCronMap.put(key, cron);
                }
                //使用CronTrigger触发器，可动态修改cron表达式来操作循环规则
                CronTrigger cronTrigger = new CronTrigger(cron);
                return cronTrigger.nextExecution(triggerContext);
            });
        });*/
    }


    /**
     * @param key
     * @param cron
     */

    public void updateCron(String key, String cron) {

        /*String s = iotProperties.getCronByTopic(key.replace("/topic/", ""));
        if (s != null) {
            taskCronMap.put(key, cron);
        }*/
        /*if (iotScanProperties.getCron().keySet().contains(key.replace("/", ""))) {
            //String s = iotScanProperties.getCronByTopic(key);
            //if (s != null) {
            taskCronMap.put(key, cron);
            //}
        }*/

    }

    @PostConstruct
    public void init() {
        log.info("init task config");
    }
}