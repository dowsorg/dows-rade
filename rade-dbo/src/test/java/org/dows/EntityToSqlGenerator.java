package org.dows;//package org.dows.framework;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class EntityToSqlGenerator {
//    private static String convertToFieldNamePublic(String fieldName) {
//        // 将字段名转换为下划线 + 小写
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < fieldName.length(); i++) {
//            char c = fieldName.charAt(i);
//            if (Character.isUpperCase(c)) {
//                if (sb.length() > 0) {
//                    sb.append("_");
//                }
//                sb.append(Character.toLowerCase(c));
//            } else {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }
//
//    public static String generateCreateTableSql(Object entity) {
//        Class<?> entityClass = entity.getClass();
//        String tableName = getTableName(entityClass);
//        List<FieldInfo> fieldInfoList = getFieldInfoList(entityClass);
//
//
//        StringBuilder sql = new StringBuilder();
//        sql.append("CREATE TABLE IF NOT EXISTS ").append(convertToFieldNamePublic(tableName)).append(" (");
//
//        String fieldsSql = fieldInfoList.stream()
//                .map(FieldInfo::toColumnDefinition)
//                .collect(Collectors.joining(",\n"));
//        sql.append(fieldsSql);
//
//        sql.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;");
//
//        return sql.toString();
//    }
//
//    private static String getTableName(Class<?> entityClass) {
//        // 从类名中提取表名,去掉"DO"后缀
//        String className = entityClass.getSimpleName();
//        return className.endsWith("DO") ? className.substring(0, className.length() - 2) : className;
//    }
//
//    private static List<FieldInfo> getFieldInfoList(Class<?> entityClass) {
//        List<Field> allFields = new ArrayList<>();
//        allFields.addAll(Arrays.asList(entityClass.getDeclaredFields()));
//        allFields.addAll(Arrays.asList(entityClass.getFields()));
//        Field[] allFieldsArray = allFields.toArray(new Field[0]);
//        return Arrays.stream(allFieldsArray)
//                .map(FieldInfo::new)
//                .collect(Collectors.toList());
//    }
//
//    private static class FieldInfo {
//        private final String name;
//        private final String type;
//        private final boolean primaryKey;
//        private final boolean autoIncrement;
//
//        public FieldInfo(Field field) {
//            this.name = convertToFieldName(field.getName());
//            this.type = getColumnType(field.getType());
//            this.primaryKey = field.isAnnotationPresent(javax.persistence.Id.class);
//            this.autoIncrement = field.isAnnotationPresent(javax.persistence.GeneratedValue.class);
//        }
//
//        private String convertToFieldName(String fieldName) {
//            // 将字段名转换为下划线 + 小写
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < fieldName.length(); i++) {
//                char c = fieldName.charAt(i);
//                if (Character.isUpperCase(c)) {
//                    if (sb.length() > 0) {
//                        sb.append("_");
//                    }
//                    sb.append(Character.toLowerCase(c));
//                } else {
//                    sb.append(c);
//                }
//            }
//            return sb.toString();
//        }
//
//        private String getColumnType(Class<?> fieldType) {
//            if (fieldType == Long.class || fieldType == long.class) {
//                return "bigint";
//            } else if (fieldType == Integer.class || fieldType == int.class) {
//                return "int";
//            } else if (fieldType == String.class||fieldType == List.class) {
//                return "varchar(255)";
//            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
//                return "tinyint(1)";
//            } else if (fieldType == java.util.Date.class || fieldType == java.sql.Date.class) {
//                return "datetime";
//            } else if (fieldType == java.time.LocalDateTime.class) {
//                return "datetime";
//            } else if (fieldType == java.math.BigDecimal.class) {
//                return "decimal(19,2)";
//            } else {
//                throw new IllegalArgumentException("Unsupported field type: " + fieldType.getName());
//            }
//        }
//
//        public String toColumnDefinition() {
//            StringBuilder columnDefinition = new StringBuilder(name + " " + type);
//            if (primaryKey) {
//                columnDefinition.append(" PRIMARY KEY");
//                if (autoIncrement) {
//                    columnDefinition.append(" AUTO_INCREMENT");
//                }
//            }
//            return columnDefinition.toString();
//        }
//    }
//
//
//    public static List<Object> createObjectsFromPackage(String packageName) {
//        List<Object> objects = new ArrayList<>();
//        String sourceDirectory = System.getProperty("user.dir");
//        if (sourceDirectory == null) {
//            sourceDirectory = System.getProperty("user.dir");
//        }
//
//        String packagePath = packageName.replace(".", File.separator);
//        File directory = new File(sourceDirectory, packagePath);
//
//        findAndCreateObjects(directory, packageName, objects);
//        return objects;
//    }
//
//    private static void findAndCreateObjects(File directory, String packageName, List<Object> objects) {
//        if (directory.isDirectory()) {
//            for (File file : directory.listFiles()) {
//                if (file.isDirectory()) {
//                    findAndCreateObjects(file, packageName + "." + file.getName(), objects);
//                } else if (file.getName().endsWith(".java")) {
//                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 5);
//                    String doPath = className.replace("src.main.java.", "");
//                    try {
//                        Class<?> clazz = Class.forName(doPath);
//                        Constructor<?> constructor = clazz.getConstructor();
//                        Object obj = constructor.newInstance();
//                        objects.add(obj);
//                    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
//                             IllegalAccessException |
//                             InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//
//
//
//    public static void main(String[] args) {
//
//        List<Object> poObjects = createObjectsFromPackage("src.main.java.org.example.dao.crm");
//
//        //这里在src/main/resources目录创建 sql.txt 文件
//        File sqlFile = new File("E:\\Idea-project\\sql_generate\\src\\main\\java\\org\\example\\dao\\crm\\sql.txt");
//        try {
//            if (!sqlFile.exists()) {
//                sqlFile.createNewFile();
//            } else {
//                // Clear the contents of the sql.txt file
//                try (FileWriter fw = new FileWriter(sqlFile, false)) {
//                    fw.write("");
//                } catch (IOException e) {
//                    System.err.println("Error clearing sql.txt file: " + e.getMessage());
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error creating sql.txt file: " + e.getMessage());
//        }
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sqlFile, true))) { // true for append mode
//            boolean isFirstStatement = true;
//            for (Object poObject : poObjects) {
//                String sql = generateCreateTableSql(poObject);
//                System.out.println(sql);
//
//                // Write the SQL statement to the sql.txt file
//                if (!isFirstStatement) {
//                    writer.newLine(); // Add newline between SQL statements
//                }
//                writer.write(sql);
//                isFirstStatement = false;
//            }
//            System.out.println("生成表数量:"+poObjects.size());
//        } catch (IOException e) {
//            System.err.println("Error writing to file: " + e.getMessage());
//        }
//
//    }
//}