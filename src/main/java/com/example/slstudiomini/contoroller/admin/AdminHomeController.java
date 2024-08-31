package com.example.slstudiomini.controller.admin;

import com.example.slstudiomini.model.User;
import com.example.slstudiomini.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String index() {
        return "admin/index";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);
        return "admin/user-list";
    }
}
