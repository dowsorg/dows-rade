package org.dows.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModelUtil {
    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <T, S> T map(S source, T target) throws MappingException {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        Assert.notNull(target, "target instance required");
        modelMapper.map(source, target);
        return target;
    }

    public static <T, S> T map(S source, Class<T> targetClass) throws MappingException {
        if (ObjectUtil.isNull(source)) {
            return null;
        }
        Assert.notNull(targetClass, "targetClass required");
        T target = modelMapper.map(source, targetClass);
        return target;
    }

    public static <T, S> List<T> map(List<S> source, Class<T> targetClass) throws MappingException {
        if (CollectionUtil.isEmpty(source)) {
            return null;
        }
        List<T> targetList = new ArrayList<T>();
        Iterator<S> iterator = source.iterator();
        while (iterator.hasNext()) {
            T target = modelMapper.map(iterator.next(), targetClass);
            targetList.add(target);
        }
        return targetList;
    }
}
