package com.example.student.miniproject;

public class Member {
    String id;
    String password;
    String name;
    int birthdate;
    String email;
    int phone;
    String address;

    public Member() {
    }

    public Member(String id, String password, String name, int birthdate, String email, int phone, String address) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(int birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
