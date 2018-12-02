package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ua.com.osht.myproject.domain.User;
import ua.com.osht.myproject.domain.dto.CaptchaResponseDto;
import ua.com.osht.myproject.service.UserService;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    final private static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user,
                          @RequestParam String passwordRetype,
                          @RequestParam("g-recaptcha-response") String captchaResponse,
                          Model model){
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        if (!response.isSuccess()){
            model.addAttribute("captchaError", "Fill captcha!");
            return "/registration";
        }

        if (!user.getPassword().equals(passwordRetype)){
            model.addAttribute("message", "Passwords are different!");
            return "/registration";
        }

        if (!userService.addUser(user)){
            model.addAttribute("message", "User with login which you input already exists!");
            return "/registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);
        if (isActivated){
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }
        return "login";
    }
}
