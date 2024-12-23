package org.dows.dbo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 回收站
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
@Schema(name = "Dbo_recycledEntity", title = "回收站")
//@TableName("dbo_recycled")
public class DboRecycledEntity {
    //@TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboRecycledId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    //@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(title = "操作人ID")
    private Long accountId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "数据ID")
    private Long dataId;

    @Schema(title = "应用ID")
    private String appId;

    @Schema(title = "表")
    private String table;

    @Schema(title = "备注")
    private String remark;

    @Schema(title = "记录数据(json格式)")
    private String dataJson;

    //@TableField(fill = FieldFill.INSERT)
    @Schema(title = "时间戳")
    private Date dt;

}