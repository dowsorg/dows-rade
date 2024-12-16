package org.dows.modules.uri.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 菜单集(RbacMenu)实体类
 *
 * @author lait
 * @since 2024-02-27 11:58:38
 */
@SuppressWarnings("serial")
@Data
@ToString
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ModuleNamespace", title = "模块命名空间")
public class UriCategoryEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "模块命名空间ID")
    private Long moduleNamespaceId;

    @Schema(title = "空间名称")
    private String menuName;
    /**
     * 菜单CODE
     */
    @Schema(title = "空间编码")
    private String menuCode;

    @Schema(title = "模块名称")
    private String moduleName;

    @Schema(title = "模块编码")
    private String moduleCode;

    @Schema(title = "menuPath")
    private String menuPath;

    @Schema(title = "state")
    private Integer state;

    /**
     * 乐观锁, 默认: 0
     */
    @Schema(title = "ver")
    private Integer ver;


    @Schema(name = "dt", title = "创建，更新，删除时间")
    private Date dt;



}