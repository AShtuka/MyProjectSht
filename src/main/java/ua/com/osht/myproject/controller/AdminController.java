package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.osht.myproject.domain.Role;
import ua.com.osht.myproject.domain.User;
import ua.com.osht.myproject.service.UserServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/adminPage")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "adminPage";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("user/{user}")
    public String userEdit(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", getRolesMap(user));
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("id") User user
    ){
        userService.userSave(username,form, user);
        return "redirect:adminPage";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
                model.addAttribute("user", user);
                return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Model model,
                                @AuthenticationPrincipal User user,
                                @RequestParam String newUsername,
                                @RequestParam String newPassword,
                                @RequestParam String passwordRetype,
                                @RequestParam String newEmail){

            if (userService.findUserByEmail(newEmail) != null){
                model.addAttribute("user", user);
                model.addAttribute("message", "User with login which you input already exists!");
                return "/profile";
            }

            if (!newPassword.equals(passwordRetype)){
                model.addAttribute("user", user);
                model.addAttribute("message", "New password and passwordRetype are different!");
                return "/profile";
            }

        userService.updateProfile(user, newUsername, newPassword, newEmail);
        return "redirect:/adminPage/profile";
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
