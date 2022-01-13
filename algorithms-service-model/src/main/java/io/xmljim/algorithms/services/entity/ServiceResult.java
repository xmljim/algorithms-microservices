package io.xmljim.algorithms.services.entity;

public interface ServiceResult<T> {
    String getDescription();

    default String getType() {
        return this.getClass().getSimpleName();
    }

    T getData();
}
