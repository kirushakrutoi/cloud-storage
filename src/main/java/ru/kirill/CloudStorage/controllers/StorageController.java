package ru.kirill.CloudStorage.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.security.UserDetailsImpl;
import ru.kirill.CloudStorage.services.MinioService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class StorageController {

    private final MinioService minioService;


    @GetMapping
    public String mainPage(@RequestParam(value = "path", required = false) String path, Model model){
        User user = getUser();
        List<String> names = new ArrayList<>();

        if(path != null)
            names = minioService.loadAll(user, path.replaceAll("%2F", "/"));
        else
            names = minioService.loadAll(user, "");

        model.addAttribute("names", names);
        model.addAttribute("path", path);

        return "main";
    }

    @PostMapping
    public String loadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam("path") String path,
                           RedirectAttributes redirectAttributes, HttpServletRequest req) throws FileNotFoundException {
        User user = getUser();

        if(path != null)
            minioService.store(file, user, path.replaceAll("%2F", "/"));
        else
            minioService.store(file, user, "");

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/newdir")
    public String loadNewDir(@RequestParam("dirname") String dirName,
                             @RequestParam("path") String path,
                             RedirectAttributes redirectAttributes, HttpServletRequest req){
        User user = getUser();

        if(path != null)
            minioService.storeDir(dirName, user, path.replaceAll("%2F", "/"));
        else
            minioService.storeDir(dirName, user, "");

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
