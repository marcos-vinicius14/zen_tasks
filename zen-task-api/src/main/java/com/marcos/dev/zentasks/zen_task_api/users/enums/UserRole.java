package com.marcos.dev.zentasks.zen_task_api.users.enums;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }


    public String getRole() {
        return role;
    }
}
