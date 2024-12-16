package org.dows.ddl.actuator.distribute;

import org.dows.ddl.actuator.sign.MysqlSign;
import org.dows.ddl.enums.SqlTypeAndJavaTypeEnum;
import org.dows.ddl.utils.SqlTypeMapUtil;

public class MysqlHandler extends AbstractDbHandler {
    @Override
    public boolean supports(Object source) {
        return source instanceof MysqlSign;
    }

    @Override
    public String doHandler(Object param) {
        SqlTypeMapUtil.ConvertBean convertBean = SqlTypeMapUtil.getInstance().typeConvert((String) param);
        if (null != convertBean) {
            return convertBean.getSqlType() + convertBean.getSqlTypeLength();
        } else {
            /*兜底配置*/
            return SqlTypeAndJavaTypeEnum.findByJavaType((String) param).getSqlType() + SqlTypeAndJavaTypeEnum.findByJavaType((String) param).getDefaultLength();
        }
    }

}
