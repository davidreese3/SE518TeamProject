package com.se518.teamproject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.serialization.ValidatingObjectInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

                //secure deserialization
                try(FileInputStream fis = new FileInputStream(tempFile);
                    ValidatingObjectInputStream vois = new ValidatingObjectInputStream(fis)) {

                    vois.accept(SimpleData.class);
                    Object obj = vois.readObject();
                    model.addAttribute("message", obj);
                    model.addAttribute("messageType", "success");
                }

                // Deserialize the file
                /*try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tempFile))) {
                    Object obj = ois.readObject();
                    model.addAttribute("message", obj.toString());
                }*/
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Error deserializing file. Class name not accepted");
                model.addAttribute("messageType", "error");
            }
            return "FileUpload"; 
        }
    
        @GetMapping("/upload") 
        public String showUploadForm(Model model) {
            model.addAttribute("message", "Upload a file to see the result.");
            return "FileUpload"; 
        }
    }
    



