package shelter.database.repository;


import shelter.database.models.Animal;
import shelter.database.models.Place;

import java.util.List;

public interface PlaceRepository {
    void save(Place place);
    List<Place> findAll();
    Place findById(Integer id);
    void delete(Integer id);
}
