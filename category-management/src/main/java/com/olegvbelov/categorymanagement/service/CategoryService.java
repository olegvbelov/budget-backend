package com.olegvbelov.categorymanagement.service;

import com.olegvbelov.categorymanagement.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategoriesForPeriod(String account, String period);
    
    void initBudget(String account, String period);
    
    void createNewPeriod(List<CategoryDto> categories, String period);
}
