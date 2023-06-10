package com.olegvbelov.core;

import com.jsoniter.any.Any;

public interface CoreBudgetService {
    String getById(String id);
    String create(Any any);
    String update(Any any);
    void deleteById(String id);
}
