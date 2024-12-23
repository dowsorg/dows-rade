package org.dows.core.status;


/**
 * 通用返回结果
 */
public enum CommonStatusCode implements StatusCode {

    /**
     * 服务器繁忙，请稍后重试
     */
    SERVER_BUSY(9998, "服务器繁忙"),
    /**
     * 服务器异常，无法识别的异常，尽可能对通过判断减少未定义异常抛出
     */
    SERVER_ERROR(9999, "网络异常"),


    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),

    // retry
    DUPLICATE_KEY(7001, "主键或唯一索引重复"),

    // json
    JSON_RESOLVE_FAILED(7940, "json 解析失败"),

    // 多租户
    NOT_TENANT_ACCOUNT(7950, "不是多租户账号"),

    // 上传
    MAX_UPLOAD_SIZE_EXCEEDED_EXCEPTION(7960, "超出最大上传尺寸"),

    // response
    RESPONSE_FAILED(7970, "响应结果为失败状态"),

    // 手机归属地与号段
    QUERY_MOBILE_SEGMENT_FAILED(7980, "查询手机号段失败"),
    MOBILE_SEGMENT_NOT_DATA(7981, "手机号段没有数据"),
    QUERY_MOBILE_INFO_FAILED(7982, "查询手机归属地失败"),
    SAVES_MOBILE_INFO_PARTIAL_FAILED(7983, "批量保存手机归属地部分失败"),
    SAVES_MOBILE_INFO_FAILED(7984, "批量保存手机归属地失败"),
    SAVES_MOBILE_SEGMENT_PARTIAL_FAILED(7985, "批量保存手机号段部分失败"),
    SAVES_MOBILE_SEGMENT_FAILED(7986, "批量保存手机号段失败"),

    // 推荐码与账号类型,
    EXTRACT_ACCOUNT_TYPE(7990, "提取账号类型失败, aid[%s]"),
    NOT_USER_OR_ARCH_SOURCE_TYPE(7991, "推荐类型不是 USER 或 ARCH 类型"),
    EXTRACT_USER_RECOMMEND_CODE_FAILED(7992, "提取 USER 推荐码失败: code[%s]"),
    QUERY_USER_RECOMMEND_CODE_FAILED(7993, "查询 USER 推荐码失败: code[%s]"),
    GENERATE_USER_RECOMMEND_CODE_FAILED(7994, "生成用户推荐关系失败: accountId[%s]"),
    SAVE_USER_RECOMMEND_RELATION_FAILED(7995, "保存用户推荐关系失败: code[%s]"),
    QUERY_MEMBER_INFO_FAILED(7996, "查询会员信息失败: memberId[%s]"),


    // 实名认证
    VERIFIED(8000, "未实名认证"),
    ABNORMALLY_TIED_CARD(8001, "实名认证身份证信息与银行绑卡信息不匹配"),


    // Time
    DATE_NOT_NULL(5001, "日期不能为空"), DATETIME_NOT_NULL(5001, "时间不能为空"), TIME_NOT_NULL(5001, "时间不能为空"),
    DATE_PATTERN_MISMATCH(5002, "日期[%s]与格式[%s]不匹配，无法解析"), PATTERN_NOT_NULL(5003, "日期格式不能为空"),
    PATTERN_INVALID(5003, "日期格式[%s]无法识别"),


    WORK_ID_GENERATE_FAILED(4000, "ID生成器的workerID已经被占满了");

    /**
     * 返回码
     */
    private final String code;
    /**
     * 返回消息
     */
    private final String descr;

    CommonStatusCode(Integer code, String descr) {
        this.code = code.toString();
        this.descr = descr;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescribe() {
        return descr;
    }

}
