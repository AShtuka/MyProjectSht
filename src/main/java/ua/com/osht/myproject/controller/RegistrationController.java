package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.osht.myproject.domain.Role;
import ua.com.osht.myproject.domain.User;
import ua.com.osht.myproject.repository.UserRepository;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam String username, @RequestParam String password, Map<String, Object> model){
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        User userFromDb = userRepository.findByUserName(user.getUserName());
        if (userFromDb != null){
            model.put("message", "User exists!");
            return "/registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
