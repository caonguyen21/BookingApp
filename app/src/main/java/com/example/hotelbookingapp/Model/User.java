package com.example.hotelbookingapp.Model;

public class User {
    public String username, email, phone, image;
    Boolean status;

    public User() {

    }

    public User(String username, String email, String phone, String image, Boolean status) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
