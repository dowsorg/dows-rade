package org.dows.dbo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 记录
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
@Schema(name = "Dbo_logEntity", title = "记录")
//@TableName("dbo_log")
public class DboLogEntity {
    //@TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboLogId;

    @Schema(title = "应用ID")
    private String appId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    //@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(title = "操作人ID")
    private Long accountId;

    @Schema(title = "请求数据")
    private String requestData;

    @Schema(title = "json[1:n],一个reqeustData可能有多个sourceJson(dml语句)")
    private String executeJson;

    //@TableField(fill = FieldFill.INSERT)
    @Schema(title = "时间戳")
    private Date dt;

}