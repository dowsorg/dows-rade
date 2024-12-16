package org.dows.ddl.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.sql.SqlUtil;
import org.dows.ddl.annotation.NotCamel;
import org.dows.ddl.annotation.Type;
import org.dows.ddl.init.Context;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public abstract class StrategyMethodUtil {
    private static final Strategy strategy = new Strategy();

    public static Object execute(Parameter p, Object arg0, Object arg1) {
        if (!p.isAnnotationPresent(NotCamel.class)) {
            return strategy.invoke(p, arg0);
        }

        if (p.isAnnotationPresent(Type.class)) {
            return strategy.invoke(p, arg0, arg1);
        }
        return arg0;
    }

    abstract Object invoke(Parameter p, Object arg0);

    abstract Object invoke(Parameter p, Object arg0, Object arg1);

    static class Strategy extends StrategyMethodUtil {
        @Override
        Object invoke(Parameter p, Object arg0) {
            if (p.getType().equals(String.class)) {
                return SqlUtil.formatSql(StrUtil.toUnderlineCase((String) arg0).toLowerCase());
            }
            if (p.getType().equals(String[].class)) {
                return Arrays.stream(((String[]) arg0))
                        .map(obj -> SqlUtil.formatSql(StrUtil.toUnderlineCase((String) arg0).toLowerCase()))
                        .toArray(String[]::new);
            }
            return arg0;
        }

        @Override
        Object invoke(Parameter p, Object arg0, Object arg1) {
            return Context.dbHandler.doHandler(arg0, arg1);
        }
    }


}
