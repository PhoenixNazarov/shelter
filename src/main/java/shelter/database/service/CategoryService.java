package shelter.database.service;

import shelter.database.exception.ValidationException;
import shelter.database.models.Category;
import shelter.database.repository.CategoryRepository;
import shelter.database.repository.impl.CategoryRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private final CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    public void createCategory(Category category) throws ValidationException {
        if (category.getName() == null) {
            throw new ValidationException("name is null");
        } else if (category.getName().trim().length() == 0) {
            throw new ValidationException("name is empty");
        } else if (!category.getName().matches("[a-z]+")) {
            throw new ValidationException("name should contains only latin lower chars");
        } else if (find(category.getName()) != null) {
            throw new ValidationException("name already use");
        }
        categoryRepository.save(category);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public Category find(String name) {
        return categoryRepository.find(name);
    }

    public Category find(Integer id) {
        return categoryRepository.find(id);
    }

    public void delete(Category category) {
        categoryRepository.delete(category.getId());
    }

    public List<String> getNames() {
        List<Category> categories = categoryRepository.findAll();
        List<String> categoriesName = new ArrayList<>();
        for (Category category: categories) {
            categoriesName.add(category.getName());
        }
        return categoriesName;
    }

}
