package org.dows.dbo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 权限
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
@Schema(name = "Dbo_permissionEntity", title = "权限")
//@TableName("dbo_permission")
public class DboPermissionEntity {
    //@TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "主键")
    private Long dboPermissionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "绑定[账号,组,角色]ID,取决于bindType")
    private Long bindId;

    @Schema(title = "应用ID")
    private String appId;

    @Schema(title = "表名")
    private String table;

    @Schema(title = "列名(逗号分割)")
    private String columns;

    @Schema(title = "其他过滤条件")
    private String filterJson;

    @Schema(title = "绑定类型[0:账号,1:组,2:角色]")
    private Integer bindType;

    //@TableField(fill = FieldFill.INSERT)
    @Schema(title = "时间戳")
    private Date dt;

}