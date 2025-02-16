package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.MyUser;
import ru.kata.spring.boot_security.demo.repositories.MyUserRepository;
import ru.kata.spring.boot_security.demo.service.MyUserServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PasswordEncoder passwordEncoder;
    private final MyUserRepository myUserRepository;
    private final MyUserServiceImpl myUserService;

    @Autowired
    public AdminController(PasswordEncoder passwordEncoder, MyUserRepository myUserRepository, MyUserServiceImpl myUserService) {
        this.passwordEncoder = passwordEncoder;
        this.myUserRepository = myUserRepository;
        this.myUserService = myUserService;
    }

    @GetMapping("/all")
    public String index(Model model) {
        model.addAttribute("users", myUserService.showAll());
        return "admin/showAll";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        MyUser optionalUser = myUserService.showById(id);
        model.addAttribute("user", optionalUser);
        return "admin/showById";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        MyUser optionalUser = myUserService.showById(id);
        model.addAttribute("user", optionalUser);
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String editUser(@PathVariable int id, @ModelAttribute MyUser updatedUser) {

        MyUser existingUser = myUserService.showById(id);

        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getAge() != 0) {
            existingUser.setAge(updatedUser.getAge());
        }
        if (updatedUser.getEmail() != null) {
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (!updatedUser.getPassword().equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        myUserRepository.save(existingUser);

        return "redirect:/admin/all";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        myUserService.deleteById(id);
        return "redirect:/admin/all";
    }
}