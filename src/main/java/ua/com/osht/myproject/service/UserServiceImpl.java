package ua.com.osht.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ua.com.osht.myproject.domain.Role;
import ua.com.osht.myproject.domain.User;
import ua.com.osht.myproject.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Override
    public boolean addUser(User user) {
        User userFromDb = findUserByEmail(user.getEmail());

        if (userFromDb != null){
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        sendMessage(user);

        return true;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to my project! Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null){
            return false;
        }
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public void userSave(String username, Map<String, String> form, User user) {
        user.setUsername(username);
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
        userRepository.save(user);
    }

    public void updateProfile(User user, String username, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (!email.equals(userEmail));

        if (isEmailChanged){
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }
    }
}
