package com.netdisk.utils;

import net.sf.ehcache.Element;
import net.sf.ehcache.store.compound.ReadWriteCopyStrategy;

import java.io.Serializable;

/**
 * 缓存策略设置
 */
public class CopyStrategy implements ReadWriteCopyStrategy<Element> {
    @Override
    public Element copyForWrite(Element value) {
        if (value != null) {
            Object temp = (Serializable) value.getObjectValue();
            return new Element(value.getObjectKey(), temp);
        }
        return value;
    }

    @Override
    public Element copyForRead(Element storedValue) {
        if (storedValue != null) {
            Object temp = (Serializable) storedValue.getObjectValue();
            return new Element(storedValue.getObjectKey(), temp);
        }
        return storedValue;
    }
}