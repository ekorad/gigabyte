package com.gigabyte.application.helpers;

public interface ChecksEntity<T> {
    boolean isEntityInvalid(T entity);
}
