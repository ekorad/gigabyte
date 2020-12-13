package com.gigabyte.application.helpers;

public interface ChecksEntity<T> {
    boolean isEntityValid(T entity);
}
