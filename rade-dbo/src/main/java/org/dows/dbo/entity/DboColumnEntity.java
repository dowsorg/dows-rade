package org.dows.dbo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 列
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
@Schema(name = "Dbo_columnEntity", title = "列")
//@TableName("dbo_column")
public class DboColumnEntity {
    //@TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboColumnId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboSourceId;

    @Schema(title = "应用ID")
    private String appId;

    @Schema(title = "表名")
    private String table;

    @Schema(title = "类型")
    private String type;

    @Schema(title = "列名")
    private String column;

    @Schema(title = "注释")
    private String comment;

    @Schema(title = "长度")
    private String length;

    @Schema(title = "复合索引名称")
    private String ikn;

    @Schema(title = "唯一索引名称")
    private String ukn;

    @Schema(title = "全文索引名称")
    private String ftn;

    @Schema(title = "页面标签")
    private String label;

    @Schema(title = "输入类型[text,select,checkbox]")
    private String inputType;

    @Schema(title = "错误提示")
    private String warning;

    @Schema(title = "占位符")
    private String placeholder;

    @Schema(title = "语言")
    private String lang;

    @Schema(title = "是否必填")
    private Integer required;

    @Schema(title = "是否主键默认fasle")
    private Integer pk;

    @Schema(title = "字段顺序")
    private Integer seq;

    @TableField(fill = FieldFill.INSERT)
    @Schema(title = "时间戳")
    private Date dt;

}