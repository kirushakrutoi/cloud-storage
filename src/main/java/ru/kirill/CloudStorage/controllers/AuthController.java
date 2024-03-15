package ru.kirill.CloudStorage.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.services.RegistrationService;
import ru.kirill.CloudStorage.validation.UserValidation;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserValidation userValidation;
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String loginPage(){

        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user){

        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user,
                               BindingResult bindingResult){

        userValidation.validate(user, bindingResult);

        if(bindingResult.hasErrors()){
            return "auth/registration";
        }

        registrationService.register(user);

        return "redirect:auth/login";
    }
}
