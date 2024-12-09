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
    private static final String UPLOAD_DIR = "/data/";
        @PostMapping("/upload")
        public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                File tempFile = new File(uploadDir, file.getOriginalFilename());
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
            return "FileUpload"; 
        }
    
        @GetMapping("/upload") 
        public String showUploadForm(Model model) {
            model.addAttribute("message", "Upload a file to see the result.");
            return "FileUpload"; 
        }
    }
    



