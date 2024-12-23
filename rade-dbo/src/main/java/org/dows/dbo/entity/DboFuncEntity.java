package org.dows.dbo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 函数
 *
 * @author lait
 * @email lait.zhang@gmail.com
 * @since 2024年12月23日 下午4:11:03
 */
@SuppressWarnings("serial")
@Data
@ToString
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Dbo_funcEntity", title = "函数")
//@TableName("dbo_func")
public class DboFuncEntity {
    //@TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboFuncId;

    @Schema(title = "应用ID")
    private String appId;

    @Schema(title = "函数名")
    private String func;

    @Schema(title = "执行器[select,insert,update,delete]")
    private String execution;

    @Schema(title = "分组")
    private String groupBy;

    @Schema(title = "排序")
    private String orderBy;

    @Schema(title = "过滤")
    private String having;

    @Schema(title = "来源表[table1,table2...](如果多表连接,第一个表为基表连接)")
    private String tables;

    @Schema(title = "连接条件{type: left,on: [table1.id = table2.id,...]}")
    private String jions;

    @Schema(title = "默认分页偏移量[0,10]")
    private String offset;

    @Schema(title = "输出对象(对表output的记录json序列化)")
    private String outputJson;

    @Schema(title = "输入对象(对表input的记录json序列化)")
    private String inputsJson;

    @Schema(title = "输入数据类型[map,list,page]")
    private String idt;

    @Schema(title = "输出数据类型[map,list,page]")
    private String odt;

    //@TableField(fill = FieldFill.INSERT)
    @Schema(title = "时间戳")
    private Date dt;

}