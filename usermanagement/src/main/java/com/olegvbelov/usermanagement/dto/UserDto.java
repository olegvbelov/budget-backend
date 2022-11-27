package com.olegvbelov.usermanagement.dto;

public class UserDto {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;

    public UserDto() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
