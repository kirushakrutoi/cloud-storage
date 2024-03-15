package ru.kirill.CloudStorage.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.security.UserDetailsImpl;
import ru.kirill.CloudStorage.services.MinioService;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class StorageController {

    private final MinioService minioService;


    @GetMapping
    public String mainPage(){
        return "main";
    }

    @PostMapping
    public String loadFile(@RequestParam("file") MultipartFile file) throws FileNotFoundException {
        User user = getUser();

        minioService.store(file, user);
        return "redirect:/";
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
