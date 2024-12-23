package org.dows.dbo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 结果集
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
@Schema(name = "Dbo_outputEntity", title = "结果集")
//@TableName("dbo_output")
public class DboOutputEntity {
    //@TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboOutputId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    //@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(title = "操作人ID")
    private Long accountId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboFuncId;

    @Schema(title = "应用ID")
    private String appId;

    @Schema(title = "表名")
    private String table;

    @Schema(title = "类型")
    private String type;

    @Schema(title = "列名")
    private String column;

    @Schema(title = "别名")
    private String alias;

    //@TableField(fill = FieldFill.INSERT)
    @Schema(title = "时间戳")
    private Date dt;

}