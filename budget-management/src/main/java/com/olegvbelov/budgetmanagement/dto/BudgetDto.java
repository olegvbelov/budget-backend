package com.olegvbelov.budgetmanagement.dto;

import java.util.Objects;

public class BudgetDto {
    private String id;
    private String userId;
    private String name;
    private String firstMonth;
    private String lastMonth;
    private String dateFormat;
    private String decimalSeparator;
    private int decimalDigits;
    private String currency;
    private String groupSeparator;
    private boolean symbolFirst;
    private boolean defaultBudget;
    private boolean isDeleted;


    public BudgetDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstMonth() {
        return firstMonth;
    }

    public void setFirstMonth(String firstMonth) {
        this.firstMonth = firstMonth;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGroupSeparator() {
        return groupSeparator;
    }

    public void setGroupSeparator(String groupSeparator) {
        this.groupSeparator = groupSeparator;
    }

    public boolean isSymbolFirst() {
        return symbolFirst;
    }

    public void setSymbolFirst(boolean symbolFirst) {
        this.symbolFirst = symbolFirst;
    }

    public boolean isDefaultBudget() {
        return defaultBudget;
    }

    public void setDefaultBudget(boolean defaultBudget) {
        this.defaultBudget = defaultBudget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BudgetDto budgetDto)) return false;
        return getDecimalDigits() == budgetDto.getDecimalDigits() && isSymbolFirst() == budgetDto.isSymbolFirst() && isDefaultBudget() == budgetDto.isDefaultBudget() && isDeleted() == budgetDto.isDeleted() && Objects.equals(getId(), budgetDto.getId()) && Objects.equals(getUserId(), budgetDto.getUserId()) && Objects.equals(getName(), budgetDto.getName()) && Objects.equals(getFirstMonth(), budgetDto.getFirstMonth()) && Objects.equals(getLastMonth(), budgetDto.getLastMonth()) && Objects.equals(getDateFormat(), budgetDto.getDateFormat()) && Objects.equals(getDecimalSeparator(), budgetDto.getDecimalSeparator()) && Objects.equals(getCurrency(), budgetDto.getCurrency()) && Objects.equals(getGroupSeparator(), budgetDto.getGroupSeparator());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getName(), getFirstMonth(), getLastMonth(), getDateFormat(), getDecimalSeparator(), getDecimalDigits(), getCurrency(), getGroupSeparator(), isSymbolFirst(), isDefaultBudget(), isDeleted());
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "BudgetDto{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", firstMonth='" + firstMonth + '\'' +
                ", lastMonth='" + lastMonth + '\'' +
                ", dateFormat='" + dateFormat + '\'' +
                ", decimalSeparator='" + decimalSeparator + '\'' +
                ", decimalDigits=" + decimalDigits +
                ", currency='" + currency + '\'' +
                ", groupSeparator='" + groupSeparator + '\'' +
                ", symbolFirst=" + symbolFirst +
                ", defaultBudget=" + defaultBudget +
                '}';
    }
}
