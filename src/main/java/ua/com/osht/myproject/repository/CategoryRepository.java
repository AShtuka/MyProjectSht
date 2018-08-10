package ua.com.osht.myproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.osht.myproject.domain.Category;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Override
    List<Category> findAll();

    Category getById(Long id);
    @Override
    void deleteById(Long aLong);
}


