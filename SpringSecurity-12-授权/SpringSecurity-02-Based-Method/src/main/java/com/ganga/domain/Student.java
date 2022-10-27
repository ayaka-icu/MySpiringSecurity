package com.ganga.domain;

public class Student {

    private String username;

    private Integer uid;

    public Student() {
    }

    public Student(String username, Integer uid) {
        this.username = username;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Student{" +
                "username='" + username + '\'' +
                ", uid=" + uid +
                '}';
    }
}
