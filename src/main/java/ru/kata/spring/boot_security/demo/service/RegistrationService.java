package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.MyUserRepository;

import java.util.Collections;

@Service
public class RegistrationService {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(MyUserRepository myUserRepository, PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(MyUser myUser) {
        myUser.setPassword(passwordEncoder.encode(myUser.getPassword()));
        myUser.setRoles(Collections.singleton(new Role(1, "ROLE_USER")));
        myUserRepository.save(myUser);
    }
}
