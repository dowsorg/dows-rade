package org.dows.sbi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/21/2024 11:44 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class SbiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SbiApplication.class, args);
    }
}

