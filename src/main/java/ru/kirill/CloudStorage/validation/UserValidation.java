package ru.kirill.CloudStorage.validation;


import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.repositories.UserRepository;
import ru.kirill.CloudStorage.services.UserDetailsServiceImpl;

@Component
@AllArgsConstructor
public class UserValidation implements Validator {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try {
            userDetailsService.loadUserByUsername(user.getEmail());
        } catch (UsernameNotFoundException ignored){
            return;
        }

        errors.rejectValue("email", "403", "A person with such an email already exists");
    }
}
