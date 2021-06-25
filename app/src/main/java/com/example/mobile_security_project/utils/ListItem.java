package com.example.mobile_security_project.utils;

public class ListItem {
    private String type;
    private String name;
    private int age;
    private String imageUrl;

    public ListItem(String head, String desc, String imageUrl, int age) {
        this.type = head;
        this.name = desc;
        this.imageUrl = imageUrl;
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
