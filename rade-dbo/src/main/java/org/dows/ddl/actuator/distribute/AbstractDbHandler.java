package org.dows.ddl.actuator.distribute;

public abstract class AbstractDbHandler {
    protected AbstractDbHandler next = null;

    public abstract boolean supports(Object source);

    public abstract String doHandler(Object param);


    public void next(AbstractDbHandler handler) {
        this.next = handler;
    }

    public static class Builder {
        private AbstractDbHandler head;
        private AbstractDbHandler tail;

        public Builder addHandler(AbstractDbHandler handler) {
            if (this.head == null) {
                this.head = handler;
                this.tail = handler;
            } else {
                this.tail.next(handler);
                this.tail = handler;
            }
            return this;
        }

        public AbstractDbHandler build() {
            return this.head;
        }

        public String doHandler(Object param, Object source) {
            if (this.head.supports(source)) {
                return this.head.doHandler(param);
            }
            if ((head = head.next) == null)
                throw new RuntimeException();
            else
                return doHandler(param, source);

        }
    }


}
