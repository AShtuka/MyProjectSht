package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestParam String password,
            @RequestParam Map<String, String> form,
            @RequestParam("id") User user
    ){
        userService.userSave(username, password, form, user);
        return "redirect:adminPage";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
                model.addAttribute("user", user);
                return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String email){
        userService.updateProfile(user, username, password, email);
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
