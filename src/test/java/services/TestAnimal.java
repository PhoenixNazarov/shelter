package services;

import shelter.database.models.Animal;
import shelter.database.models.Category;
import shelter.database.models.Place;
import shelter.database.service.AnimalService;
import shelter.database.service.CategoryService;
import shelter.database.service.PlaceService;

import java.util.Date;
import java.util.List;

public class TestAnimal {
    public static void main(String[] args) {
        AnimalService animalService = new AnimalService();

        CategoryService categoryService = new CategoryService();
        PlaceService placeService = new PlaceService();

        Category c = categoryService.getCategories().get(0);
        Place p = placeService.getPlaces().get(0);

        Animal animal = new Animal();
        animal.setCategoryId(c.getId());
        animal.setPlaceId(p.getId());

        animal.setName("qweqwe");
        animal.setDescription("rqwrqwrqwr");
        animal.setVaccinated(false);
        animal.setTimeStart(new Date());

        List<Animal> al = animalService.getAnimals();

        animalService.createAnimal(animal);

        List<Animal> a = animalService.getAnimals();
        Animal newAnimal = a.get(a.size() - 1);

        if (al.size()+1 != a.size()) {
            throw new RuntimeException();
        }

        if (!newAnimal.getId().equals(animal.getId())) {
            throw new RuntimeException();
        }
        if (!newAnimal.getName().equals(animal.getName())) {
            throw new RuntimeException();
        }
        if (!newAnimal.getTimeStart().toString().equals(animal.getTimeStart().toString())) {
            throw new RuntimeException();
        }
        if (!newAnimal.getPlaceId().equals(p.getId())) {
            throw new RuntimeException();
        }
        if (!newAnimal.getCategoryId().equals(c.getId())) {
            throw new RuntimeException();
        }
        if (!newAnimal.getVaccinated().equals(animal.getVaccinated())) {
            throw new RuntimeException();
        }

    }
}
