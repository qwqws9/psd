package xyz.dunshow.entity;

import xyz.dunshow.util.ObjectMapperUtils;

public class BaseEntity {

    public <T> T toDto(Class<T> type) {
        return ObjectMapperUtils.map(this, type);
    }
}
