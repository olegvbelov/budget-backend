package com.olegvbelov.core.util;

import javax.servlet.http.HttpServletRequest;

public class BudgetUtils {
    public static String extractPathVariable(HttpServletRequest req) {
        var id = req.getPathInfo();
        return id.substring(id.lastIndexOf("/") + 1);
    }
}
