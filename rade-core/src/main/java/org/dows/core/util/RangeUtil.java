package org.dows.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2023/2/2 9:57
 */
public class RangeUtil {

    /**
     * 开闭区间正则表达式
     */
    private static final Pattern NUM_RANGE_PATTERN = Pattern.compile("[\\[|\\(]\\s?\\d+\\s?,\\s?\\d+\\s?[\\)|\\]]");

    /**
     * 左半区间正则表达式
     */
    private static final Pattern LEFT_NUM_RANGE_PATTERN = Pattern.compile("[\\[|\\(]\\s?\\d+\\s?,\\s?[\\)|\\]]");

    /**
     * 右半区间正则表达式
     */
    private static final Pattern RIGHT_NUM_RANGE_PATTERN = Pattern.compile("[\\[|\\(],\\s?\\d+\\s?[\\)|\\]]");

    /**
     * 判断数值是否在区间范畴内
     *
     * @param number   数值
     * @param numRange 开闭区间
     * @return boolean
     */
    public static boolean inNumRange(int number, String numRange) {
        Objects.requireNonNull(numRange);

        if (!isValidNumRange(numRange)) {
            return false;
        }

        String[] pairs = numRange.split(",");

        // 获取开闭区间的最小值和最大值
        List<String> rangeNums = Arrays.stream(pairs).map(str -> str.replaceAll("[(|)|\\[|\\]]", "").trim()).collect(Collectors.toList());
        Integer minValue = "".equals(rangeNums.get(0)) ? null : Integer.valueOf(rangeNums.get(0));
        Integer maxValue = "".equals(rangeNums.get(1)) ? null : Integer.valueOf(rangeNums.get(1));

        // 断定数值是否大于最小值
        boolean minMatched = (minValue == null) || (pairs[0].startsWith("[") ? number >= minValue : number > minValue);
        // 断定数值是否小于最大值
        boolean maxMatched = (maxValue == null) || (pairs[1].endsWith("]") ? number <= maxValue : number < maxValue);

        return minMatched && maxMatched;
    }

    /**
     * 判断是否为无效的数字区间范畴
     *
     * @param numRange 数字区间
     * @return boolean
     */
    public static boolean isValidNumRange(String numRange) {
        return NUM_RANGE_PATTERN.matcher(numRange).matches()
                || LEFT_NUM_RANGE_PATTERN.matcher(numRange).matches()
                || RIGHT_NUM_RANGE_PATTERN.matcher(numRange).matches();
    }
}
