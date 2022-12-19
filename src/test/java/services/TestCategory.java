package services;

import shelter.database.models.Category;
import shelter.database.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

public class TestCategory {
    public static void main(String[] args) {
        CategoryService categoryService = new CategoryService();

        Category category1 = new Category();
        category1.setName("dog");

        Category category2 = new Category();
        category2.setName("cat");

        try {
            categoryService.createCategory(category1);
        } catch (Exception ignored) {

        }
        try {
            categoryService.createCategory(category2);
        } catch (Exception ignored) {

        }

        if (categoryService.getCategories().stream().noneMatch(n -> n.getName().equals("dog"))) {
            throw new RuntimeException();
        }
        if (categoryService.getCategories().stream().noneMatch(n -> n.getName().equals("cat"))) {
            throw new RuntimeException();
        }

        List<Category> c = categoryService.getCategories();
        Category cc = c.get(c.size() - 1);


        if (!categoryService.find(cc.getName()).getName().equals(cc.getName())) {
            throw new RuntimeException();
        }

        if (!categoryService.find(cc.getId()).getName().equals(cc.getName())) {
            throw new RuntimeException();
        }

    }
}
