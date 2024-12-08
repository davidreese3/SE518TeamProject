package com.se518.teamproject;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

public class MaliciousData implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;

    public MaliciousData() {
        this.content = "Default Malicious Content";
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.content = "Unexpected Content from Malicious File!";
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Content: " + content;
    }
}

