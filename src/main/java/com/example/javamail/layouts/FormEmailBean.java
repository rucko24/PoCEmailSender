package com.example.javamail.layouts;

import com.example.javamail.util.UploadComponent;

public class FormEmailBean {

    private String email;
    private String password;
    private String description;
    private String forUser;
    private UploadComponent uploadComponent;

    public UploadComponent getUploadComponent() {
        return uploadComponent;
    }

    public void setUploadComponent(UploadComponent uploadComponent) {
        this.uploadComponent = uploadComponent;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FormEmailBean{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", description='" + description + '\'' +
                ", forUser='" + forUser + '\'' +
                ", uploadComponent=" + uploadComponent +
                '}';
    }
}
