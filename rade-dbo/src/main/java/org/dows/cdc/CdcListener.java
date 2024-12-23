package org.dows.cdc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.message.MessageHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CdcListener {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private final ApplicationContext applicationContext;
    private final CdcConfiguration cdcConfiguration;
    private final static Map<String, DebeziumEngine<ChangeEvent<String, String>>> engines = new HashMap<>();

//    @Override
//    public void run(String... args) throws Exception {
//        Set<String> keys = engines.keySet();
//        for (String key : keys) {
//            start(key);
//            //DebeziumEngine<ChangeEvent<String, String>> engine = engines.get(key);
//        }
//    }


    @PostConstruct
    public void init() {
        List<CdcJobProperties> jobs = cdcConfiguration.getJobs();
        for (CdcJobProperties job : jobs) {
            if (!job.isEnable()) {
                continue;
            }
            Properties configuration = cdcConfiguration.debeziumConfig(job);
            if(null != configuration) {
                DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
                        .using(configuration)
                        .notifying(changeEvent -> receiveChangeEvent(changeEvent, job))
                        .build();
//            threadPoolTaskExecutor.execute(engine);
                engines.put(job.getName(), engine);
                // 开启job
                start(job.getName());
            }
        }
    }

    private void handleEvent(List<ChangeEvent<String, String>> changeEvents, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> changeEventRecordCommitter) {
        log.info(JSONUtil.toJsonStr(changeEvents));
    }


    private void receiveChangeEvent(ChangeEvent<String, String> changeEvent, CdcJobProperties jobProperties /* String name, String dbType, Class<? extends MessageHandler<? extends Message>> handleClass*/) {
        String name = jobProperties.getName();
        String dbType = jobProperties.getDbType();
        Class<?> handleClass = null;
        try {
            String shandleClass = jobProperties.getHandleClass();
            handleClass = Class.forName(shandleClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        String value = changeEvent.value();
        if (Objects.nonNull(value)) {
            JSON jsonValue = JSONUtil.parse(value);
            String handleType = getHandleType(jsonValue);
            if (!("NONE".equals(handleType) || "READ".equals(handleType))) {
                ChangeData changeData = buildChangeData(jsonValue);
                Map<String, Object> data;
                if ("DELETE".equals(handleType)) {
                    data = changeData.getBefore();
                } else {
                    data = changeData.getAfter();
                }
                CdcMessage cdcMessage = CdcMessage.builder()
                        .data(changeData)
                        .dbType(dbType)
                        .database(String.valueOf(changeData.getSource().get("db")))
                        .table(String.valueOf(changeData.getSource().get("table")))
                        .handleType(handleType)
                        .build();
                log.info("receive message : {}{}", name, cdcMessage.toString());
                Object bean = applicationContext.getBean(handleClass);
                if(bean instanceof MessageHandler){
                    ((MessageHandler) bean).handle(cdcMessage);
                }
            }
        }
    }


    public static String getHandleType(JSON jsonValue) {
        String path = ".payload.op";
        Object byPath = JSONUtil.getByPath(jsonValue, path);
        if (byPath == null) {
            return "NONE";
        }
        String op = byPath.toString();
        if (Objects.nonNull(op)) {
            return switch (op) {
                case "r" -> "READ";
                case "c" -> "CREATE";
                case "u" -> "UPDATE";
                case "d" -> "DELETE";
                default -> "NONE";
            };
        } else {
            return "NONE";
        }
    }

    public static ChangeData buildChangeData(JSON jsonValue) {
        Object after = JSONUtil.getByPath(jsonValue, ".payload.after");
        Object source = JSONUtil.getByPath(jsonValue, ".payload.source");
        Object before = JSONUtil.getByPath(jsonValue, ".payload.before");
        return ChangeData.builder()
                .after(BeanUtil.beanToMap(after))
                .source(BeanUtil.beanToMap(source))
                .before(BeanUtil.beanToMap(before))
                .build();
    }


    public void start(String jobName) {
        DebeziumEngine<ChangeEvent<String, String>> engine = engines.get(jobName);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(engine);
        threadPoolTaskExecutor.execute(engine);
    }


    public void stop(String jobName) {
        DebeziumEngine<ChangeEvent<String, String>> engine = engines.get(jobName);
        try {
            engine.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @PreDestroy
    private void destroy() {
        Set<String> keySet = engines.keySet();
        for (String key : keySet) {
            stop(key);
        }
    }


}
