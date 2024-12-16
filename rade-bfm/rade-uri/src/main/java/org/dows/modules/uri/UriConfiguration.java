//package org.dows.modules.uri;
//
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.annotation.PostConstruct;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.ConversionService;
//import org.springframework.core.type.filter.AnnotationTypeFilter;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
//
//import java.util.*;
//
///**
// * @description: </br>
// * @author: lait.zhang@gmail.com
// * @date: 3/20/2024 9:14 AM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
//
//@Slf4j
//@RequiredArgsConstructor
//@EnableConfigurationProperties({ModuleProperties.class})
//@Configuration
//@Data
//public class UriConfiguration {
//
//    private final ModuleProperties moduleProperties;
//    private final RequestMappingInfoHandlerMapping requestMappingHandlerMapping;
//    private final ModuleNamespaceRepository moduleNamespaceRepository;
//    private final ModuleUriRepository moduleUriRepository;
//    private final ModuleHandler moduleHandler;
//    private final ConversionService conversionService;
//
//
//    @PostConstruct
//    public void init() {
//        if (!moduleProperties.isInit()) {
//            return;
//        }
//        List<ScanResource> resources = moduleProperties.getResources();
//
//        Map<RequestMappingInfo, HandlerMethod> handlerMethodsMap = requestMappingHandlerMapping.getHandlerMethods();
//        // 记录method->requestMappingInfo
//        Map<HandlerMethod, RequestMappingInfo> requestMappingInfoHashMap = new HashMap<>();
//        Map<Class<?>, List<HandlerMethod>> classHandlerMethodMap = new HashMap<>();
//        handlerMethodsMap.forEach((requestMappingInfo, handlerMethod) -> {
//            List<HandlerMethod> handlerMethods = classHandlerMethodMap
//                    .computeIfAbsent(handlerMethod.getBeanType(), k -> new ArrayList<>());
//            requestMappingInfoHashMap.put(handlerMethod, requestMappingInfo);
//            handlerMethods.add(handlerMethod);
//        });
//
//        Map<Class<?>, List<HandlerMethod>> newClassHandlerMethodMap = new HashMap<>();
//        for (ScanResource resource : resources) {
//            List<String> scanPackages = resource.getScanPackages();
//            for (String scanPackage : scanPackages) {
//                List<Class<?>> classes = scanNamespace(scanPackage);
//                for (Class<?> aClass : classes) {
//                    List<HandlerMethod> handlerMethods = classHandlerMethodMap.get(aClass);
//                    if (handlerMethods != null) {
//                        newClassHandlerMethodMap.put(aClass, handlerMethods);
//                    }
//                }
//            }
//        }
//        Map<ModuleNamespaceEntity, List<ModuleUriEntity>> entityListMap = new HashMap<>();
//
//        newClassHandlerMethodMap.forEach((clazz, methods) -> {
//            Namespace menu = clazz.getAnnotation(Namespace.class);
//            if (null != menu) {
//                ModuleNamespaceEntity moduleNamespaceEntity = ModuleNamespaceEntity.builder()
//                        .moduleCode(menu.module())
//                        .menuName(menu.name())
//                        .menuCode(menu.code())
//                        .menuPath(menu.path())
//                        .build();
//                List<ModuleUriEntity> moduleUriEntities = new ArrayList<>();
//                for (HandlerMethod method : methods) {
//                    Operation methodAnnotation = method.getMethodAnnotation(Operation.class);
//                    String summary = method.toString();
//                    if (methodAnnotation != null) {
//                        summary = methodAnnotation.summary();
//                    }
//                    // 拿到该接口方法的请求方式(GET、POST等)
//                    RequestMappingInfo requestMappingInfo = requestMappingInfoHashMap.get(method);
//                    Set<RequestMethod> methodSet = requestMappingInfo.getMethodsCondition().getMethods();
//                    // 如果一个接口方法标记了多个请求方式，权限id是无法识别的，不进行处理
//                    if (methodSet.size() != 1) {
//                        return;
//                    }
//                    // 将请求方式和路径用`:`拼接起来，以区分接口。比如：GET:/user/{id}、POST:/user/{id}
//                    String httpMethod = methodSet.toArray()[0].toString();
//                    PathPatternsRequestCondition pathPatternsCondition = requestMappingInfo.getPathPatternsCondition();
//                    if (pathPatternsCondition != null) {
//                        String path = pathPatternsCondition.getPatterns().toArray()[0] + "";
//                        ModuleUriEntity moduleUriEntity = ModuleUriEntity.builder()
//                                .uriName(summary)
//                                .descr(method.toString())
//                                .methodName(httpMethod)
//                                .url(path)
//                                .build();
//                        moduleUriEntities.add(moduleUriEntity);
//                    }
//                }
//                entityListMap.put(moduleNamespaceEntity, moduleUriEntities);
//            }
//        });
//        // todo init 初始化
//        initDatas(entityListMap);
//    }
//
//
//    /**
//     * 初始化数据
//     *
//     * @param entityListMap
//     */
//    public void initDatas(Map<ModuleNamespaceEntity, List<ModuleUriEntity>> entityListMap) {
//        List<ModuleNamespaceEntity> insertNamespaceEntities = new ArrayList<>();
//        List<ModuleNamespaceEntity> updateNamespaceEntities = new ArrayList<>();
//        List<ModuleUriEntity> uriEntities = new ArrayList<>();
//        entityListMap.forEach((k, v) -> {
//            ModuleNamespaceEntity menus = moduleHandler.getByMenuCode(k.getModuleCode(), k.getMenuCode());
//            Long id = null;
//            if(null == menus){
//                id = IdWorker.getId();
//                k.setModuleNamespaceId(id);
//                insertNamespaceEntities.add(k);
//            }else{
//                id = menus.getModuleNamespaceId();
//                menus.setMenuName(k.getMenuName());
//                menus.setMenuPath(k.getMenuPath());
//                menus.setModuleCode(k.getModuleCode());
//                updateNamespaceEntities.add(menus);
//            }
//            for (ModuleUriEntity moduleUriEntity : v) {
//                moduleUriEntity.setModuleNamespaceId(id);
//                String[] split = moduleUriEntity.getDescr().split("#");
//                moduleUriEntity.setUriCode(k.getMenuCode()+"."+split[1]);
//                uriEntities.add(moduleUriEntity);
//            }
//        });
//        moduleHandler.saveModuleNamespace(insertNamespaceEntities);
//        moduleHandler.updateModuleNamespace(updateNamespaceEntities);
//        moduleHandler.saveOrUpdateModuleUri(uriEntities);
//    }
//
//
//    /**
//     * 扫描菜单
//     *
//     * @param basePackage
//     * @return
//     */
//    public List<Class<?>> scanNamespace(String basePackage) {
//        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
//        provider.addIncludeFilter(new AnnotationTypeFilter(Namespace.class));
//        Set<BeanDefinition> components = provider.findCandidateComponents(basePackage);
//
//        List<Class<?>> classes = new ArrayList<>();
//        for (BeanDefinition component : components) {
//            try {
//                classes.add(Class.forName(component.getBeanClassName()));
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return classes;
//    }
//
//}