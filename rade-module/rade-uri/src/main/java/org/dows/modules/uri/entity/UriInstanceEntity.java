package org.dows.modules.uri.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 接口集(RbacUri)实体类
 *
 * @author lait
 * @since 2024-02-27 11:58:39
 */
@SuppressWarnings("serial")
@Data
@ToString
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ModuleUri", title = "接口集")
public class UriInstanceEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(name = "moduleUriId", title = "资源id")
    private Long moduleUriId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(name = "moduleNamespaceId", title = "菜单ID")
    private Long moduleNamespaceId;

    @Schema(name = "uriName", title = "资源名称")
    private String uriName;

    @Schema(name = "uriCode", title = "资源CODE")
    private String uriCode;

    @Schema(name = "label", title = "页面功能标签[按钮、链接]")
    private String label;

    @Schema(name = "url", title = "资源链接")
    private String url;

    @Schema(name = "configJson", title = "json数据集")
    private String configJson;

    @Schema(name = "descr", title = "描述")
    private String descr;

    @Schema(name = "methodName", title = "方法名")
    private String methodName;

    @Schema(name = "state", title = "状态")
    private Integer state;

    @Schema(name = "ver", title = "乐观锁, 默认: 0")
    private Integer ver;

    @Schema(name = "shared", title = "是否共享[0:不共享,1:共享]")
    private Boolean shared;

    @Schema(name = "dt", title = "创建，更新，删除时间")
    private Date dt;

}