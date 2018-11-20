package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.osht.myproject.domain.Role;
import ua.com.osht.myproject.domain.User;
import ua.com.osht.myproject.service.UserServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adminPage")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "adminPage";
    }

    @GetMapping("user/{user}")
    public String userEdit(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", getRolesMap(user));
        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam Map<String, String> form,
            @RequestParam("id") User user
    ){
        user.setUserName(username);
        user.setPassword(password);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        user.setAdmin(false);
        for (String key : form.keySet()){
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
                if (Role.valueOf(key).equals(Role.ADMIN)){
                    user.setAdmin(true);
                }
            }
        }
        userService.saveUser(user);
        return "redirect:adminPage";
    }

    private Map<Role, Boolean> getRolesMap(User user){
        Role[] allRoles = Role.values();
        Set<Role> userRoles = user.getRoles();
        Map<Role, Boolean> roles = new HashMap<>();
        for (Role role : allRoles){
            roles.put(role, false);
        }
        for (Role role : userRoles){
            if (roles.containsKey(role)){
                roles.replace(role, true);
            }
        }
        return roles;
    }
}
