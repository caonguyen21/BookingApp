package com.example.adminbookingapp.Model;

public class Owner {
    String username, email, phone, image, tenks;

    public Owner() {

    }

    public Owner(String username, String email, String phone, String image, String tenks) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.tenks = tenks;
    }

    public String getTenks() {
        return tenks;
    }

    public void setTenks(String tenks) {
        this.tenks = tenks;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
