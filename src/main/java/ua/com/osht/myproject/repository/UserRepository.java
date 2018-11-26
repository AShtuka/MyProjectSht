package ua.com.osht.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.osht.myproject.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String userEmail);
    User findByActivationCode(String code);
}
