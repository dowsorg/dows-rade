//package org.dows.ddl.builder.mysql;
//
//import org.dows.ddl.actuator.ExecuteCreate;
//import org.dows.ddl.actuator.ibuilder.CreateBuilder;
//import org.dows.ddl.actuator.sign.MysqlSign;
//
//public class CreateMysqlBuilder extends ExecuteCreate<CreateMysqlBuilder> implements MysqlSign, CreateBuilder<CreateMysqlBuilder> {
//    @Override
//    public CreateMysqlBuilder create() {
//        ddl.append("CREATE");
//        return this.space();
//    }
//
//    @Override
//    public CreateMysqlBuilder addField(String field, String type, boolean isPrimaryKey) {
//        ddl.append(field).append(type);
//        if (isPrimaryKey) {
//            ddl.append(" AUTO_INCREMENT PRIMARY KEY");
//        } else {
//            ddl.append("  NULL");
//        }
//        ddl.append(",");
//        return this;
//    }
//
//    @Override
//    public CreateMysqlBuilder addField(String field, String type) {
//        return addField(field, type, false);
//    }
//
//    @Override
//    public CreateMysqlBuilder addType(String type) {
//        ddl.append(type);
//        return this;
//    }
//
//    @Override
//    public CreateMysqlBuilder isPrimaryKey(boolean isPrimaryKey) {
//        if (isPrimaryKey) {
//            ddl.append("AUTO_INCREMENT PRIMARY KEY");
//        } else {
//            ddl.append(" NULL");
//        }
//        return this;
//    }
//
//    @Override
//    public CreateMysqlBuilder remove() {
//        ddl.deleteCharAt(ddl.length() - 1);
//        return this;
//    }
//
//    @Override
//    public CreateMysqlBuilder remove(int size) {
//        ddl.delete(ddl.length() - size, ddl.length());
//        return this;
//    }
//
//    @Override
//    public CreateMysqlBuilder addComment(String commend) {
//        ddl.append("COMMENT ").append("'").append(commend).append("'");
//        return this;
//    }
//
//
//    public CreateMysqlBuilder dataBase(String dataBase) {
//        ddl.append(" DATABASE ").append(dataBase);
//        return this.space();
//    }
//}
