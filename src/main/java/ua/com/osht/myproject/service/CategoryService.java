package ua.com.osht.myproject.service;

import ua.com.osht.myproject.domain.Category;

import java.util.List;


public interface CategoryService {
    Category getCategoryById(Long id);
    void saveCategory(Category category);
    void updateCategory(Long id, String categoryName);
    void deleteCategory(Long id);
    List<Category> findByUserId(Long id);

}
