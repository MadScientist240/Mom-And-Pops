package com.bobatea.momandpops.backend.models;

public class Employee {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
    private String dob;
    private String bankingInfo;
    private String employeeType;
    private String role;
    private double salary;

    public Employee(int id, String name, String phone, String email, String address, String password,
                    String dob, String bankingInfo, String employeeType, String role, double salary) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
        this.dob = dob;
        this.bankingInfo = bankingInfo;
        this.employeeType = employeeType;
        this.role = role;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBankingInfo() {
        return bankingInfo;
    }

    public void setBankingInfo(String bankingInfo) {
        this.bankingInfo = bankingInfo;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
