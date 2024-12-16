package org.dows.ddl.actuator.distribute;

import org.dows.ddl.actuator.sign.Db2Sign;

public class Db2Handler extends AbstractDbHandler {
    @Override
    public boolean supports(Object source) {
        return source instanceof Db2Sign;
    }

    @Override
    public String doHandler(Object param) {
        return null;
    }
}
