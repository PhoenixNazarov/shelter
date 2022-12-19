package shelter.database.repository;


import shelter.database.models.Animal;

import java.util.List;

public interface AnimalRepository {
    void save(Animal animal);
    List<Animal> finAll();
    Animal find(Integer id);
    void delete(Integer id);
    void setVaccinate(Integer id, Boolean value);
}
