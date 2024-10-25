package org.dows.core.leaf;

public interface IDGenService {
    long next(String key);
    void init();
}
