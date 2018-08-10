package ua.com.osht.myproject.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.osht.myproject.domain.Category;
import ua.com.osht.myproject.repository.CategoryRepository;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository repository;

    @Override
    public Category getCategoryById(Long id) {
        return repository.getById(id);
    }

    @Override
    public void saveCategory(Category category) {
        repository.save(category);
    }

    @Override
    public void updateCategory(Long id, String categoryName) {
        Category updated = repository.getById(id);
        updated.setCategoryName(categoryName);
        repository.save(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

}
