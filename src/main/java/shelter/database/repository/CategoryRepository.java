package shelter.database.repository;


import shelter.database.models.Category;

import java.util.List;

public interface CategoryRepository {
    void save(Category category);
    Category find(Integer id);
    Category find(String name);
    List<Category> findAll();
    void delete(Integer id);
}
