package com.netdisk.utils;

import net.sf.ehcache.Element;
import net.sf.ehcache.store.compound.ReadWriteCopyStrategy;

import java.io.Serializable;

/**
 * @ClassName: CacheUtil
 * @Description: 缓存策略
 * @Date: 2022/4/28 17:55
 */
public class CacheUtil implements ReadWriteCopyStrategy<Element> {
    @Override
    public Element copyForWrite(Element element){
        if (element!=null){
            Object temp =(Serializable)element.getObjectValue();
            return new Element(element.getObjectKey(),temp);
        }
        else return element;
    }

    @Override
    public Element copyForRead(Element element) {
        if(element!=null){
            Object temp =(Serializable)element.getObjectValue();
            return new Element(element.getObjectKey(),temp);
        }
        else return element;
    }
}
