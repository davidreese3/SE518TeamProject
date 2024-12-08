package com.se518.teamproject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

@Controller
public class FileUploadController {
        @PostMapping("/upload")
        public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
            try {
                // Save the uploaded file temporarily
                File tempFile = new File("/data/" + file.getOriginalFilename());
                file.transferTo(tempFile);
    
                // Deserialize the file
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tempFile))) {
                    Object obj = ois.readObject();
                    model.addAttribute("message", "Successfully deserialized: " + obj.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Error deserializing file: " + e.getMessage());
            }
            return "FileUpload"; // Return the view name
        }
    
        @GetMapping("/upload") // Correct method for serving the form
        public String showUploadForm(Model model) {
            model.addAttribute("message", "Upload a file to see the result.");
            return "FileUpload"; // Return the HTML form view
        }
    }
    



