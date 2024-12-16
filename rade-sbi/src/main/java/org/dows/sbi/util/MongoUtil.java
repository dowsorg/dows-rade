package org.dows.sbi.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.dows.sbi.pojo.MongoSetting;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MongoUtil {

    private static final Map<String, MongoClient> CLIENTS = new ConcurrentHashMap<>();
    private static final Map<String, String> RP = new ConcurrentHashMap<>();
    private final static String PASSWORD = "pwd";
    private final static String ROLES = "roles";
    private final static String CREATE_USER = "createUser";
    private static final String DROP_USER = "dropUser";
    private final static String USERS = "users";
    private final static String USER_INFO = "usersInfo";

    public final static String PERMISSION = "readWrite";

    /**
     * 定义一个getMongoClient()方法，用于获取MOngoDB数据库的连接对象
     *
     * @param configSetting
     */
    public static void initClient(MongoSetting configSetting) {
        String username = configSetting.getUsername();
        String password = configSetting.getPassword();
        String host = configSetting.getHost();
        String port = configSetting.getPort();
        String addr = null;
        String s = RP.get(username);
        if (s == null) {
            username = null;
            password = null;
        } else {
            password = s;
        }
        if (username == null && password == null) {
            addr = String.format("mongodb://%s:%s/admin", host, port);
        } else {
            addr = String.format("mongodb://%s:%s@%s:%s/admin", username, password, host, port);
            RP.put(username, password);
        }
        MongoUtil.CLIENTS.put(host, MongoClients.create(addr));
        //mongoClient = MongoClients.create(addr);
        //return mongoClient;
    }


    /**
     * 定义一个getMongoConn()方法，用于实现连接指定的MongoDB数据库
     *
     * @param host
     * @param database
     * @return
     */
    public static MongoDatabase getMongoConn(String host, String database) {
        MongoClient mongoClient = CLIENTS.get(host);
        if (mongoClient != null) {
            return mongoClient.getDatabase(database);
        } else {
            throw new RuntimeException("mongo host not exit");
        }
    }

    public static MongoIterable<String> getDBs(String host) {
        MongoClient mongoClient = MongoUtil.CLIENTS.get(host);
        if (mongoClient != null) {
            return mongoClient.listDatabaseNames();
        } else {
            throw new RuntimeException("mongo host not exit");
        }
    }

    // 查询用户
    public static FindIterable<Document> getUsers(String host, String databaseName) {
        MongoClient mongoClient = MongoUtil.CLIENTS.get(host);
        if (mongoClient != null) {
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            return db.getCollection("users").find();
        } else {
            throw new RuntimeException("mongo host not exit");
        }
    }

    // 查询某个用户信息
    public static List<Document> getUserInfo(String host, String databaseName, String userName) {
        MongoClient mongoClient = MongoUtil.CLIENTS.get(host);
        if (mongoClient != null) {
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            Document usersInfo = db.runCommand(new Document(USER_INFO, userName));
            return (List<Document>) usersInfo.get(USERS);
        } else {
            throw new RuntimeException("mongo host not exit");
        }
    }

    /**
     * 添加用户
     *
     * @param mongoSetting
     * @param roles
     */
    public static void addUser(MongoSetting mongoSetting, String roles) {
        MongoClient mongoClient = MongoUtil.CLIENTS.get(mongoSetting.getHost());
        if (mongoClient != null) {
            MongoDatabase db = mongoClient.getDatabase(mongoSetting.getDatabaseName());
            Document usersInfo = db.runCommand(new Document(USER_INFO, mongoSetting.getUsername()));
            List users = (List) usersInfo.get(USERS);
            if (CollectionUtil.isEmpty(users)) {
                // 创建用户
                db.runCommand(new BasicDBObject(CREATE_USER, mongoSetting.getUsername())
                        .append(PASSWORD, mongoSetting.getPassword())
                        .append(ROLES, Arrays.asList(roles)));
            }
        } else {
            throw new RuntimeException("mongo host not exit");
        }

    }

    /**
     * 修改用户密码的方法，如果用户不存在则创建新用户
     *
     * @param mongoSetting
     */
    public static void updateUserPassword(MongoSetting mongoSetting) {
        MongoClient mongoClient = null;
        try {
//            SbiUtil.UsernamePassword usernamePassword = SbiUtil.getUsernamePassword(mongoSetting);
            String username = null;
            String password = null;
            String host = mongoSetting.getHost();
            String port = mongoSetting.getPort();
            String addr = null;
            if(StrUtil.isBlank(password)){
                addr = String.format("mongodb://%s:%s/admin", host, port);
            }else{
                addr = String.format("mongodb://%s:%s@%s:%s/admin", username, password, host, port);
            }
            mongoClient = MongoClients.create(addr);
            MongoUtil.CLIENTS.put(host, mongoClient);
            String databaseName = mongoSetting.getDatabaseName();
            String userName = mongoSetting.getUsername();
            String newPassword = mongoSetting.getPassword();
            if (mongoClient != null) {
                MongoDatabase db = mongoClient.getDatabase(databaseName);
                // 检查用户是否存在
                Document userCheck = db.runCommand(new Document(USER_INFO, userName));
                List users = (List) userCheck.get(USERS);
                if (CollectionUtil.isEmpty(users)) {
                    // 用户不存在，创建新用户
                    db.runCommand(new BasicDBObject(CREATE_USER, userName)
                            .append(PASSWORD, newPassword)
                            .append(ROLES, Arrays.asList(PERMISSION)));
                    log.info("User '{}' created successfully in database '{}'", userName, databaseName);
                } else {
                    // 用户存在，更新用户密码
                    db.runCommand(new BasicDBObject("updateUser", userName)
                            .append(PASSWORD, newPassword));
                    log.info("Password updated successfully for user '{}' in database '{}'", userName, databaseName);
                }
//                Files.writeString(usernamePassword.rpPath(), String.format("%s,%s", userName, usernamePassword.newPassword()), StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                throw new RuntimeException("mongo host not exit");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if (mongoClient != null) {
                mongoClient.close();
            }
        }

    }

    // 向指定数据库的指定集合插入数据
    public static void insertData(String host, String databaseName, String collectionName, Object data) {
        if (databaseName == null || databaseName.isEmpty()) {
            throw new IllegalArgumentException("Database name cannot be null or empty");
        }
        if (collectionName == null || collectionName.isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data to insert cannot be null");
        }
        MongoDatabase database = getMongoConn(host, databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        try {
            // 将Object转换为JSON字符串，然后解析为Document
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(data);
            Document document = Document.parse(json);
            collection.insertOne(document);
            log.info("Data inserted successfully into database '{}', collection '{}'", databaseName, collectionName);
        } catch (IOException e) {
            log.error("Failed to serialize object to JSON: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Failed to serialize object to JSON", e);
        } catch (Exception e) {
            log.error("Failed to insert data into database '{}', collection '{}'", databaseName, collectionName, e);
            throw e;
        }
    }

    // 删除用户

    public static void dropUser(String host, String databaseName, String userName) {
        MongoClient mongoClient = CLIENTS.get(host);
        if (mongoClient != null) {
            MongoDatabase db = mongoClient.getDatabase(databaseName);
            db.runCommand(new BasicDBObject(DROP_USER, userName));
        } else {
            throw new RuntimeException("mongo host not exit");
        }
    }

    // 关闭连接
    public static void close(MongoSetting configSetting) {
        MongoClient mongoClient = CLIENTS.get(configSetting.getHost());
        if (mongoClient != null) {
            mongoClient.close();
        } else {
            throw new RuntimeException("mongo host not exit");
        }
    }
}