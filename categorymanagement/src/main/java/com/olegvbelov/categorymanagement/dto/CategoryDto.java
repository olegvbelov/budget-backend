package com.olegvbelov.categorymanagement.dto;

import java.math.BigDecimal;

public class CategoryDto {
    private String id;
    private String name;
    private String parentId;
    private String createPeriod;
    private String closePeriod;
    private long sortOrder;
    private String fundsId;
    private String period;
    private BigDecimal allocatedFunds;
    private BigDecimal usedFunds;
    private BigDecimal availableFunds;
    
    public CategoryDto() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getParentId() {
        return parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getCreatePeriod() {
        return createPeriod;
    }
    
    public void setCreatePeriod(String createPeriod) {
        this.createPeriod = createPeriod;
    }
    
    public String getClosePeriod() {
        return closePeriod;
    }
    
    public void setClosePeriod(String closePeriod) {
        this.closePeriod = closePeriod;
    }
    
    public long getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(long sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getFundsId() {
        return fundsId;
    }
    
    public void setFundsId(String fundsId) {
        this.fundsId = fundsId;
    }
    
    public String getPeriod() {
        return period;
    }
    
    public void setPeriod(String period) {
        this.period = period;
    }
    
    public BigDecimal getAllocatedFunds() {
        return allocatedFunds;
    }
    
    public void setAllocatedFunds(BigDecimal allocatedFunds) {
        this.allocatedFunds = allocatedFunds;
    }
    
    public BigDecimal getUsedFunds() {
        return usedFunds;
    }
    
    public void setUsedFunds(BigDecimal usedFunds) {
        this.usedFunds = usedFunds;
    }
    
    public BigDecimal getAvailableFunds() {
        return availableFunds;
    }
    
    public void setAvailableFunds(BigDecimal availableFunds) {
        this.availableFunds = availableFunds;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryDto)) return false;
        
        CategoryDto that = (CategoryDto) o;
    
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return "CategoryDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", createPeriod='" + createPeriod + '\'' +
                ", closePeriod='" + closePeriod + '\'' +
                ", sortOrder=" + sortOrder +
                ", balanceId='" + fundsId + '\'' +
                ", period='" + period + '\'' +
                ", allocatedFunds=" + allocatedFunds +
                ", usedFunds=" + usedFunds +
                ", availableFunds=" + availableFunds +
                '}';
    }
}
