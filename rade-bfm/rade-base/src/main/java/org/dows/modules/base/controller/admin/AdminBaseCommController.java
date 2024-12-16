//package org.dows.modules.base.controller.admin;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.dows.core.aid.RadeAid;
//import org.dows.core.annotation.RadeController;
//import org.dows.core.annotation.TokenIgnore;
//import org.dows.core.plugin.upload.FileUploadStrategyFactory;
//import org.dows.core.web.Response;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * 系统通用接口， 每个人都有权限操作
// */
//@RequiredArgsConstructor
//@Tag(name = "系统通用", description = "系统通用")
//@RadeController()
//public class AdminBaseCommController {
//
////    final private BaseSysPermsService baseSysPermsService;
////
////    final private BaseSysUserService baseSysUserService;
////
////    final private BaseSysLoginService baseSysLoginService;
//
//    final private RadeAid radeAid;
//
//    final private FileUploadStrategyFactory fileUploadStrategyFactory;
//
//
//    /*@Operation(summary = "个人信息")
//    @GetMapping("/person")
//    public Response person(@RequestAttribute() Long adminUserId) {
//        BaseSysUserEntity baseSysUserEntity = baseSysUserService.getById(adminUserId);
//        baseSysUserEntity.setPassword(null);
//        baseSysUserEntity.setPasswordV(null);
//        return Response.ok(baseSysUserEntity);
//    }
//
//    @Operation(summary = "修改个人信息")
//    @PostMapping("/personUpdate")
//    public Response personUpdate(@RequestAttribute Long adminUserId, @RequestBody Dict body) {
//        baseSysUserService.personUpdate(adminUserId, body);
//        return Response.ok();
//    }
//
//    @Operation(summary = "权限与菜单")
//    @GetMapping("/permmenu")
//    public Response permmenu(@RequestAttribute() Long adminUserId) {
//        return Response.ok(baseSysPermsService.permmenu(adminUserId));
//    }
//
//
//    @Operation(summary = "退出")
//    @PostMapping("/logout")
//    public Response logout(@RequestAttribute Long adminUserId, @RequestAttribute String adminUsername) {
//        baseSysLoginService.logout(adminUserId, adminUsername);
//        return Response.ok();
//    }*/
//
//
//   /* @TokenIgnore
//    @Operation(summary = "实体信息与路径", description = "系统所有的实体信息与路径，供前端自动生成代码与服务")
//    @GetMapping("/aid")
//    public Response aid() {
//        return Response.ok(radeAid.getAdmin());
//    }*/
//
////    @TokenIgnore
////    @Operation(summary = "编程")
////    @GetMapping("/program")
////    public Response program() {
////        return Response.ok("Java");
////    }
//
//
///*    @Operation(summary = "文件上传")
//    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
//    public Response upload(@RequestPart(value = "upload", required = false)
//                           @Parameter(description = "文件") MultipartFile[] files, HttpServletRequest request) {
//        return Response.ok(fileUploadStrategyFactory.upload(files, request));
//    }
//
//    @Operation(summary = "文件上传模式")
//    @GetMapping("/uploadMode")
//    public Response uploadMode() {
//        return Response.ok(fileUploadStrategyFactory.getMode());
//    }*/
//}
