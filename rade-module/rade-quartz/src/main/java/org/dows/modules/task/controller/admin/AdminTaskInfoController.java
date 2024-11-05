package org.dows.modules.task.controller.admin;

import cn.hutool.json.JSONObject;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.web.Response;
import org.dows.modules.task.entity.TaskInfoEntity;
import org.dows.modules.task.service.TaskInfoService;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import jakarta.servlet.http.HttpServletRequest;

import static org.dows.modules.task.entity.table.TaskInfoEntityTableDef.TASK_INFO_ENTITY;

/**
 * 任务
 */
@Tag(name = "任务管理", description = "统一管理任务")
@RadeController(api = {"add", "delete", "update", "info", "page"})
public class AdminTaskInfoController extends BaseController<TaskInfoService, TaskInfoEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(createOp().fieldEq(TASK_INFO_ENTITY.STATUS, TASK_INFO_ENTITY.TYPE));
    }

    @Operation(summary = "执行一次")
    @PostMapping("/once")
    public Response once(@RequestAttribute JSONObject requestParams) {
        service.once(requestParams.getLong("id"));
        return Response.ok();
    }

    @Operation(summary = "开始任务")
    @PostMapping("/start")
    public Response start(@RequestAttribute JSONObject requestParams) {
        service.start(requestParams.getLong("id"), requestParams.getInt("type"));
        return Response.ok();
    }

    @Operation(summary = "停止任务")
    @PostMapping("/stop")
    public Response stop(@RequestAttribute JSONObject requestParams) {
        service.stop(requestParams.getLong("id"));
        return Response.ok();
    }

    @Operation(summary = "任务日志")
    @GetMapping("/log")
    public Response log(@RequestAttribute JSONObject requestParams) {
        Integer page = requestParams.getInt("page", 0);
        Integer size = requestParams.getInt("size", 20);
        return Response.ok(pageResult((Page<TaskInfoEntity>) service.log(new Page<>(page, size), requestParams.getLong("id"),
                requestParams.getInt("status"))));
    }
}
