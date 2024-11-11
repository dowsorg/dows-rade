package org.dows;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//@SpringBootTest(classes = RadeApplication.class)
public class Cdc {

    private static final String JDBC_URL = "jdbc:mysql://192.168.111.103:3306/ddd?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8";
    private static final String USERNAME = "shdy";
    private static final String PASSWORD = "shdy123!";

    @Test
    public void test() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // 获取数据库中的所有表名
            List<String> tableNames = getTableNames(connection);

            // 假设要查找的appId值，这里你可以根据实际需求修改或通过参数传入
            String appIdToSearch = "12345";

            for (String tableName : tableNames) {
                int count = countAppIdRecords(connection, tableName, appIdToSearch);
                if (count > 0) {
                    // 生成INSERT语句，这里假设表结构简单，只有几列，你需要根据实际表结构修改
                    String insertStatement = generateInsertStatement(connection, tableName, appIdToSearch);
                    System.out.println(insertStatement);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW TABLES")) {

            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }
        }
        return tableNames;
    }

    private static int countAppIdRecords(Connection connection, String tableName, String appIdToSearch) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM " + tableName + " WHERE `app_id` = '" + appIdToSearch + "'")) {

            if (resultSet.next()) {
                System.out.println("==============" + resultSet.getInt("count"));
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println(tableName + " --- 没有app_id");
        }
        return 0;
    }

    private static String generateInsertStatement(Connection connection, String tableName, String appIdToSearch) throws SQLException {
        StringBuilder insertStatement = new StringBuilder("INSERT INTO " + tableName + " (`app_id`, `dt`, `account_id`) VALUES ");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT `app_id`, `dt`, `account_id` FROM " + tableName + " WHERE `app_id` = '" + appIdToSearch + "'")) {

            boolean firstRow = true;
            while (resultSet.next()) {
                if (!firstRow) {
                    insertStatement.append(", ");
                }
                insertStatement.append("\n");
                insertStatement.append("('");
                insertStatement.append(resultSet.getString("app_id"));
                insertStatement.append("', '");
                insertStatement.append(resultSet.getString("dt"));
                insertStatement.append("', '");
                insertStatement.append(resultSet.getString("account_id"));
                insertStatement.append("')");

                firstRow = false;
            }
        }

        return insertStatement.append("\n").toString();
    }
}


