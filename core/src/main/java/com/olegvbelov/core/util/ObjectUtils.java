package com.olegvbelov.core.util;

public class ObjectUtils {
    public static Boolean isNullOrBlank(String str) {
        return str == null || str.isBlank() || str.isEmpty();
    }
}
