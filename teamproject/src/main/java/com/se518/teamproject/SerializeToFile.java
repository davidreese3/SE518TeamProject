package com.se518.teamproject;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeToFile {

    public static void main(String[] args) {
        try {
            // Serialize expected data
            SimpleData simpleData = new SimpleData("Original Text");
            serializeToFile(simpleData, "legitimate_data.txt");

            // Serialize malicious data
            MaliciousData maliciousData = new MaliciousData();
            serializeToFile(maliciousData, "malicious_data.txt");

            System.out.println("Serialized files have been created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void serializeToFile(Serializable object, String fileName) throws Exception {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(object);
        }
    }
}

