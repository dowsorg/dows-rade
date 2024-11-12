package org.dows;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppIdDataExtractor {

    private static final String JDBC_URL = "jdbc:mysql://192.168.111.103:3306/ddd?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2b8";
    private static final String USERNAME = "shdy";
    private static final String PASSWORD = "shdy123!";

    // 定义DDL文件的输出路径，可根据实际情况修改
    private static final String DDL_FILE_PATH = "E:\\database_ddl.sql";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // 获取数据库中的所有表名
            List<String> tableNames = getTableNames(connection);

            // 生成DDL文件
            generateDDLFile(connection, tableNames);

            // 假设要查找的appId值，这里你可以根据实际需求修改或通过参数传入
            String appIdToSearch = "12345";

            String appId = "123456";

            for (String tableName : tableNames) {
                // 判断表中是否存在app_id列
                boolean hasAppIdColumn = doesTableHaveAppIdColumn(connection, tableName);

                if (hasAppIdColumn) {
                    int count = countAppIdRecords(connection, tableName, appIdToSearch);
                    if (count > 0) {
                        // 生成INSERT语句，通过查询表结构获取列名来动态生成，并为列名和值加上反引号
                        String insertStatement = generateInsertStatement(connection, tableName, appIdToSearch,appId);
                        System.out.println(insertStatement);
                    }
                } else {
                    // 如果表中不存在app_id列，生成该表的全部INSERT语句，并为列名和值加上反引号
                    String insertStatementForAll = generateInsertStatementForAll(connection, tableName,appId);
                    System.out.println(insertStatementForAll);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    private static boolean doesTableHaveAppIdColumn(Connection connection, String tableName) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW COLUMNS FROM " + tableName)) {

            while (resultSet.next()) {
                if ("app_id".equals(resultSet.getString("Field"))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int countAppIdRecords(Connection connection, String tableName, String appIdToSearch) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM " + tableName + " WHERE app_id = '" + appIdToSearch + "'")) {

            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        }
        return 0;
    }

    private static String generateInsertStatement(Connection connection, String tableName, String appIdToSearch,String newAppId) throws SQLException {
        // 查询表结构获取列名
        List<String> columnNames = getColumnNames(connection, tableName);

        StringBuilder insertStatement = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < columnNames.size(); i++) {
            if (i > 0) {
                insertStatement.append(", ");
            }
            insertStatement.append("`").append(columnNames.get(i)).append("`");
        }
        insertStatement.append(") VALUES ");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE app_id = '" + appIdToSearch + "'")) {

            boolean firstRow = true;
            while (resultSet.next()) {
                if (!firstRow) {
                    insertStatement.append(", ");
                }
                insertStatement.append("(");
                for (int i = 0, n = columnNames.size(); i < n; i++) {
                    if (i > 0) {
                        insertStatement.append(", ");
                    }
                    if(columnNames.get(i).equals("app_id")){
                        insertStatement.append("'").append(newAppId).append("'");
                    } else {
                        insertStatement.append("'");
                        insertStatement.append(resultSet.getString(columnNames.get(i)));
                        insertStatement.append("'");
                    }
                    /*insertStatement.append("'");
                    insertStatement.append(resultSet.getString(columnNames.get(i)));
                    insertStatement.append("'");*/
                }
                insertStatement.append(")");
                firstRow = false;
            }
        }

        return insertStatement.toString();
    }

    private static String generateInsertStatementForAll(Connection connection, String tableName,String appId) throws SQLException {
        // 查询表结构获取列名
        List<String> columnNames = getColumnNames(connection, tableName);

        Map<String,Integer> IDS = new HashMap<>();
        StringBuilder insertStatement = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < columnNames.size(); i++) {
            if (i > 0) {
                insertStatement.append(", ");
            }
            // 定位是第几个
            if(columnNames.get(i).equals("app_id")){
                IDS.put("app_id",i);
            }
            insertStatement.append("`").append(columnNames.get(i)).append("`");
        }
        insertStatement.append(") VALUES ");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {

            boolean firstRow = true;
            while (resultSet.next()) {
                if (!firstRow) {
                    insertStatement.append(", ");
                }
                insertStatement.append("(");
                for (int i = 0, n = columnNames.size(); i < n; i++) {
                    if (i > 0) {
                        insertStatement.append(", ");
                    }

                    if(columnNames.get(i).equals("app_id")){
                        insertStatement.append("'").append(appId).append("'");
                    } else {
                        insertStatement.append("'");
                        insertStatement.append(resultSet.getString(columnNames.get(i)));
                        insertStatement.append("'");
                    }
                }
                insertStatement.append(")");
                firstRow = false;
            }
        }

        return insertStatement.toString();
    }

    private static List<String> getColumnNames(Connection connection, String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW COLUMNS FROM " + tableName)) {

            while (resultSet.next()) {
                columnNames.add(resultSet.getString("Field"));
            }
        }
        return columnNames;
    }

    private static void generateDDLFile(Connection connection, List<String> tableNames) throws SQLException, IOException {
        try (FileWriter writer = new FileWriter(DDL_FILE_PATH)) {
            for (String tableName : tableNames) {
                // 获取表的创建语句
                String createTableStatement = getCreateTableStatement(connection, tableName);
                writer.write(createTableStatement + ";\n");
            }
        }
    }

    private static String getCreateTableStatement(Connection connection, String tableName) throws SQLException {
        StringBuilder createTableStmt = new StringBuilder("DROP TABLE IF EXISTS `"+tableName+"`;\nCREATE TABLE ");
        createTableStmt.append(tableName).append(" (");

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW COLUMNS FROM " + tableName)) {

            boolean firstColumn = true;
            while (resultSet.next()) {
                if (!firstColumn) {
                    createTableStmt.append(", ");
                }
                String columnName = resultSet.getString("Field");
                String dataType = resultSet.getString("Type");
                String nullable = resultSet.getString("Null");
                String key = resultSet.getString("Key");
                String defaultValue = resultSet.getString("Default");
                String extra = resultSet.getString("Extra");

                createTableStmt.append("`").append(columnName).append("` ");
                createTableStmt.append(dataType);
                if ("NO".equals(nullable)) {
                    createTableStmt.append(" NOT NULL");
                }
                if ("PRI".equals(key)) {
                    createTableStmt.append(" PRIMARY KEY");
                }
                if (defaultValue!= null) {
                    createTableStmt.append(" DEFAULT ").append(defaultValue);
                }
                if ("auto_increment".equals(extra)) {
                    createTableStmt.append(" AUTO_INCREMENT");
                }

                firstColumn = false;
            }
            createTableStmt.append(")");
        }

        return createTableStmt.toString();
    }
}