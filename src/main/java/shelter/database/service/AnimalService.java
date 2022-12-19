package shelter.database.service;

import shelter.database.exception.ValidationException;
import shelter.database.models.Animal;
import shelter.database.models.Category;
import shelter.database.models.Place;
import shelter.database.repository.impl.AnimalRepositoryImpl;
import shelter.database.repository.AnimalRepository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AnimalService {
    private final AnimalRepository animalRepository = new AnimalRepositoryImpl();

    private final CategoryService categoryService = new CategoryService();
    private final PlaceService placeService = new PlaceService();


    public void createAnimal(Animal animal) throws ValidationException {
        // validate
        if (animal.getCategoryName() == null) {
            throw new ValidationException("category is empty");
        } else if (animal.getPlaceNumber() == null) {
            throw new ValidationException("place number is empty");
        } else if (animal.getName() == null) {
            throw new ValidationException("name is empty");
        } else if (animal.getName().trim().equals("")) {
            throw new ValidationException("name is empty");
        }

        Place place = placeService.findByNumber(animal.getPlaceNumber());
        Category category = categoryService.find(animal.getCategoryName());

        animal.setVaccinated(false);
        animal.setPlaceId(place.getId());
        animal.setCategoryId(category.getId());
        animal.setTimeStart(new Date());

        animalRepository.save(animal);
    }

    public List<Animal> getAnimals() {
        List<Animal> animals = animalRepository.finAll();
        List<Place> places = placeService.getPlaces();
        List<Category> categories = categoryService.getCategories();
        for (int i = 0; i < animals.size(); i++) {
            Animal myAnimal = animals.get(i);
            for (Place place : places) {
                if (Objects.equals(place.getId(), myAnimal.getPlaceId())) {
                    myAnimal.setPlaceNumber(place.getNumber());
                    break;
                }
            }

            for (Category category : categories) {
                if (Objects.equals(category.getId(), myAnimal.getCategoryId())) {
                    myAnimal.setCategoryName(category.getName());
                    break;
                }
            }
            if (myAnimal.getPlaceNumber() == null) {
                myAnimal.setPlaceNumber(-1);
            }
            if (myAnimal.getCategoryName() == null) {
                myAnimal.setCategoryName("undefined");
            }
        }

        animals.sort(Comparator.comparing(Animal::getId));

        return animals;
    }

    public Animal findById(Integer id) {
        Animal animal = animalRepository.find(id);
        List<Place> places = placeService.getPlaces();
        List<Category> categories = categoryService.getCategories();

        for (Place place : places) {
            if (Objects.equals(place.getId(), animal.getPlaceId())) {
                animal.setPlaceNumber(place.getNumber());
            }
        }

        for (Category category : categories) {
            if (Objects.equals(category.getId(), animal.getCategoryId())) {
                animal.setCategoryName(category.getName());
            }
        }
        return animal;
    }

    public void delete(Animal animal) {
        animalRepository.delete(animal.getId());
    }

    public void setVaccinate(Animal animal, Boolean value) {
        animalRepository.setVaccinate(animal.getId(), value);
    }

}
