package org.dows.cdc;//package org.dows.task.handler.cdc;
//
//import cn.hutool.json.JSONObject;
//import com.ververica.cdc.connectors.shaded.org.apache.kafka.connect.source.SourceRecord;
//import com.ververica.cdc.debezium.DebeziumDeserializationSchema;
//import io.debezium.data.Envelope;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.flink.wrapper.common.typeinfo.TypeInformation;
//import org.apache.flink.util.Collector;
//import org.apache.kafka.connect.data.Field;
//import org.apache.kafka.connect.data.Schema;
//import org.apache.kafka.connect.data.Struct;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.Optional;
//
//
///**
// * @description: SQLServer消息读取自定义序列化
// * @author: lait.zhang@gmail.com
// * @date: 7/2/2024 5:18 PM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
//@Slf4j
//public class JsonDebeziumDeserializationSchema implements DebeziumDeserializationSchema<WorkOrderInfo> {
//
//    public static final String TS_MS = "ts_ms";
//    public static final String BEFORE = "before";
//    public static final String AFTER = "after";
//    public static final String SOURCE = "source";
//    public static final String CREATE = "CREATE";
//    public static final String UPDATE = "UPDATE";
//
//
//    /**
//     * 反序列化数据,转为变更JSON对象
//     */
//    @Override
//    public void deserialize(SourceRecord sourceRecord, Collector<WorkOrderInfo> collector) {
//        try {
//            String topic = sourceRecord.topic();
//            String[] fields = topic.split("\\.");
//            String database = fields[1];
//            String tableName = fields[2];
//            Struct struct = (Struct) sourceRecord.value();
//            final Struct source = struct.getStruct(SOURCE);
//            WorkOrderInfo workOrderInfo = new WorkOrderInfo();
//            workOrderInfo.setBeforeData(getJsonObject(struct, BEFORE).toString());
//            workOrderInfo.setAfterData(getJsonObject(struct, AFTER).toString());
//            // 获取操作类型  CREATE UPDATE DELETE  1新增 2修改 3删除
//            Envelope.Operation operation = Envelope.operationFor(sourceRecord);
//            String type = operation.toString().toUpperCase();
//            int eventType = type.equals(CREATE) ? 1 : UPDATE.equals(type) ? 2 : 3;
//            workOrderInfo.setEventType(eventType);
//            workOrderInfo.setDatabase(database);
//            workOrderInfo.setTableName(tableName);
//            ZoneId zone = ZoneId.systemDefault();
//            Long timestamp = Optional.ofNullable(struct.get(TS_MS)).map(x -> Long.parseLong(x.toString())).orElseGet(System::currentTimeMillis);
//            workOrderInfo.setChangeTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zone));
//            //7.输出数据
//            collector.collect(workOrderInfo);
//        } catch (Exception e) {
//            log.error("SQLServer消息读取自定义序列化报错：{}", e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 从源数据获取出变更之前或之后的数据
//     */
//    private JSONObject getJsonObject(Struct value, String fieldElement) {
//        Struct element = value.getStruct(fieldElement);
//        JSONObject jsonObject = new JSONObject();
//        if (element != null) {
//            Schema afterSchema = element.schema();
//            List<Field> fieldList = afterSchema.fields();
//            for (Field field : fieldList) {
//                Object afterValue = element.get(field);
//                jsonObject.set(field.name(), afterValue);
//            }
//        }
//        return jsonObject;
//    }
//
//
//    @Override
//    public TypeInformation<WorkOrderInfo> getProducedType() {
//        return TypeInformation.of(WorkOrderInfo.class);
//    }
//
//}