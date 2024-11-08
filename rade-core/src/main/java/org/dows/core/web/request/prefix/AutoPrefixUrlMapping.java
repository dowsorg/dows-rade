package org.dows.core.web.request.prefix;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.annotation.RadeController;
import org.dows.core.util.ConvertUtil;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * 自动配置模块的路由
 */
@Slf4j
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RadeController[] annotations = handlerType.getAnnotationsByType(RadeController.class);
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        String packageName = handlerType.getPackage().getName();
        if (info != null && annotations.length > 0 && annotations[0].value().length == 0
                && packageName.contains("modules")) {
            if (!checkApis(annotations, info)) {
                return null;
            }
            String prefix = getPrefix(packageName);
            String cName = getCName(annotations[0].cname(), handlerType, prefix);
            info = info.mutate().paths(prefix + "/" + cName).build().combine(info);
        }
        return info;
    }

    /**
     * 根据配置检查是否构建路由
     *
     * @param annotations 注解
     * @param info        路由信息
     * @return 是否需要构建路由
     */
    private boolean checkApis(RadeController[] annotations, RequestMappingInfo info) {
        String[] apis = new String[]{"add", "delete", "remove", "update", "page", "list", "info"};
        if (info.getPathPatternsCondition() == null) {
            return true;
        }
        List<String> setApis;
        if (ArrayUtil.isNotEmpty(annotations)) {
            RadeController radeController = annotations[0];
            setApis = CollUtil.toList(radeController.api());

            Set<String> methodPaths = info.getPathPatternsCondition().getPatternValues();
            String methodPath = methodPaths.iterator().next().replace("/", "");
            if (!CollUtil.toList(apis).contains(methodPath)) {
                return true;
            } else {
                return setApis.contains(methodPath);
            }
        }
        return false;
    }

    /**
     * 根据Controller名称构建路由地址
     *
     * @param handlerType 类
     * @param prefix      路由前缀
     * @return url地址
     */
    private String getCName(String cname, Class<?> handlerType, String prefix) {
        if (ObjUtil.isNotEmpty(cname)) {
            return cname;
        }
        String name = handlerType.getName();
        String[] names = name.split("[.]");
        name = names[names.length - 1];
        cname = name.replace(ConvertUtil.pathToClassName(prefix), "")
                .replace("Controller", "");

        String[] split = prefix.split("/");
        if(split.length>=2){
            // todo 此处可以动态化映射
            cname = cname.replace(StrUtil.upperFirst(split[1]), "");
        }
        return ConvertUtil.classNameToPath(cname);
    }

    /**
     * 构建路由前缀
     *
     * @param packageName 包名
     * @return 返回路由前缀
     */
    private String getPrefix(String packageName) {
        String dotPath = packageName.split("modules")[1]; // 将包路径中多于的部分截取掉
        String[] dotPaths = dotPath.replace(".controller", "").split("[.]");
        List<String> paths = CollUtil.toList(dotPaths);
        paths.removeIf(String::isEmpty);
        // 第一和第二位互换位置
        String p0 = paths.get(0);
        String p1 = paths.get(1);
        paths.set(0, p1);
        paths.set(1, p0);
        dotPath = "/" + CollUtil.join(paths, "/");
        return dotPath;
    }
}
