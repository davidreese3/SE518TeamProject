package com.se518.teamproject;

import java.io.Serializable;

public class SimpleData implements Serializable{
    private static final long serialVersionUID = 1L;

    private String content;

    public SimpleData(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Content: " + content;
    }
}
