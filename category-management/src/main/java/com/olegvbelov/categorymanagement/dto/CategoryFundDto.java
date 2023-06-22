package com.olegvbelov.categorymanagement.dto;

import com.olegvbelov.core.dto.BaseDto;

import java.math.BigDecimal;
import java.util.Objects;

public class CategoryFundDto extends BaseDto {
    private String categoryId;
    private String month;
    private BigDecimal budgeted;
    private BigDecimal used;
    private BigDecimal balance;

    public CategoryFundDto() {
        this.balance = BigDecimal.ZERO;
        this.used = BigDecimal.ZERO;
        this.budgeted = BigDecimal.ZERO;
    }

    public CategoryFundDto(String id, String categoryId, String month, BigDecimal budgeted, BigDecimal used, BigDecimal balance) {
        this.id = id;
        this.categoryId = categoryId;
        this.month = month;
        this.budgeted = budgeted;
        this.used = used;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getBudgeted() {
        return budgeted;
    }

    public void setBudgeted(BigDecimal budgeted) {
        this.budgeted = budgeted;
    }

    public BigDecimal getUsed() {
        return used;
    }

    public void setUsed(BigDecimal used) {
        this.used = used;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryFundDto that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCategoryId(), that.getCategoryId()) && Objects.equals(getMonth(), that.getMonth()) && Objects.equals(getBudgeted(), that.getBudgeted()) && Objects.equals(getUsed(), that.getUsed()) && Objects.equals(getBalance(), that.getBalance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCategoryId(), getMonth(), getBudgeted(), getUsed(), getBalance());
    }
}
