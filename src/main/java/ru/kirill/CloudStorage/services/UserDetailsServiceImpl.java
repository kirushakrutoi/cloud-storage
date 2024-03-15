package ru.kirill.CloudStorage.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kirill.CloudStorage.models.User;
import ru.kirill.CloudStorage.repositories.UserRepository;
import ru.kirill.CloudStorage.security.UserDetailsImpl;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> OUser = userRepository.findByEmail(username);

        if(OUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserDetailsImpl(OUser.get());
    }
}
