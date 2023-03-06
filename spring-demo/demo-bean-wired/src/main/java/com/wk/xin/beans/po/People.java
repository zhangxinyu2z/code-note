package com.wk.xin.beans.po;

import lombok.*;

import java.util.*;

/**
 * @author Arnoer
 * @since 2022/9/27 11:29
 */
@Setter@Getter
@ToString
public class People {
    private String name;

    private int age;
    
    private Heart heart;

    private String[] hobbies;

    private List<String> loveActress;

    private Map<String,String> loveStarCode;

    private Properties properties;

    public People() {
    }

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public People(String name, int age, Heart heart) {
        this.name = name;
        this.age = age;
        this.heart = heart;
    }

    public People(String name, int age, Heart heart, String[] hobbies, List<String> loveActress, Map<String, String> loveStarCode, Properties properties) {
        this.name = name;
        this.age = age;
        this.heart = heart;
        this.hobbies = hobbies;
        this.loveActress = loveActress;
        this.loveStarCode = loveStarCode;
        this.properties = properties;
    }
}
