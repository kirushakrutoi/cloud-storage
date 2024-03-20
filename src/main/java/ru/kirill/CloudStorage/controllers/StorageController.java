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
import ru.kirill.CloudStorage.services.interfaces.StorageService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class StorageController {

    private final StorageService storageService;


    @GetMapping
    public String mainPage(@RequestParam(value = "path", required = false) String path, Model model){
        User user = getUser();
        List<String> names = new ArrayList<>();

        if(path != null)
            names = storageService.loadAll(user, path.replaceAll("%2F", "/"));
        else
            names = storageService.loadAll(user, "");

        model.addAttribute("names", names);
        model.addAttribute("path", path);

        return "main";
    }

    @PostMapping("/upload/file")
    public String loadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam("path") String path,
                           RedirectAttributes redirectAttributes) throws FileNotFoundException {
        User user = getUser();

        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());


        if(path != null)
            storageService.store(file, user, path.replaceAll("%2F", "/"));
        else
            storageService.store(file, user, "");

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/upload/folder")
    public String loadFolder(@RequestParam("file") List<MultipartFile> files,
                           @RequestParam("path") String path,
                           RedirectAttributes redirectAttributes) throws FileNotFoundException {
        User user = getUser();

        for(MultipartFile file : files){
            System.out.println(file.getOriginalFilename());
        }


        if(path != null)
            storageService.uploadFolder(files, user, path.replaceAll("%2F", "/"));
        else
            storageService.uploadFolder(files, user, "");

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/newdir")
    public String loadNewDir(@RequestParam("dirname") String dirName,
                             @RequestParam("path") String path,
                             RedirectAttributes redirectAttributes){
        User user = getUser();

        if(path != null)
            storageService.storeDir(dirName, user, path.replaceAll("%2F", "/"));
        else
            storageService.storeDir(dirName, user, "");

        redirectAttributes.addAttribute("path", path);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam("path") String path,
                             @RequestParam("filename") String filename,
                             RedirectAttributes redirectAttributes){

        User user = getUser();

        if(path != null) {
            storageService.delete(filename, user, path.replaceAll("%2F", "/"));
            redirectAttributes.addAttribute("path", path);
        }
        else
            storageService.delete(filename, user, "");


        return "redirect:/";
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails.getUser();
    }
}
