package org.dows.modules.plugin.controller.admin;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.annotation.IgnoreRecycleData;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.plugin.service.RadePluginService;
import org.dows.core.web.Response;
import org.dows.modules.plugin.entity.PluginInfoEntity;
import org.dows.modules.plugin.service.PluginInfoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.dows.modules.plugin.entity.table.PluginInfoEntityTableDef.PLUGIN_INFO_ENTITY;

@Tag(name = "插件信息", description = "插件信息")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
@RequiredArgsConstructor
public class AdminPluginInfoController extends BaseController<PluginInfoService, PluginInfoEntity> {

    final private RadePluginService radePluginService;

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(createOp()
                .queryWrapper(QueryWrapper.create().orderBy(PLUGIN_INFO_ENTITY.UPDATE_TIME, false))
                .select(PLUGIN_INFO_ENTITY.DEFAULT_COLUMNS));
    }

    @Override
    @Operation(summary = "修改", description = "根据ID修改")
    @PostMapping("/update")
    protected Response update(@RequestBody PluginInfoEntity t,
                              @RequestAttribute() JSONObject requestParams) {
        radePluginService.updatePlugin(t);
        return Response.ok();
    }

    @Operation(summary = "安装插件")
    @PostMapping(value = "/install", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response install(
            @RequestParam(value = "files") @Parameter(description = "文件") MultipartFile[] files,
            @RequestParam(value = "force") @Parameter(description = "是否强制安装") boolean force) {
        radePluginService.install(files[0], force);
        return Response.ok();
    }

    @Override
    @Operation(summary = "卸载插件")
    @PostMapping("/delete")
    @IgnoreRecycleData()
    public Response delete(HttpServletRequest request, @RequestBody Map<String, Object> params,
                           @RequestAttribute() JSONObject requestParams) {
        radePluginService.uninstall(Convert.toLongArray(getIds(params))[0]);
        return Response.ok();
    }
}
