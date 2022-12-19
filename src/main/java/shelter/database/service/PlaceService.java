package shelter.database.service;

import shelter.database.exception.ValidationException;
import shelter.database.models.Animal;
import shelter.database.models.Place;
import shelter.database.repository.AnimalRepository;
import shelter.database.repository.PlaceRepository;
import shelter.database.repository.impl.AnimalRepositoryImpl;
import shelter.database.repository.impl.PlaceRepositoryImpl;

import java.util.*;

public class PlaceService {
    private final PlaceRepository placeRepository = new PlaceRepositoryImpl();

    public void createPlace(Place place) throws ValidationException {
        if (place.getDescription().length() > 64) {
            throw new ValidationException("description should be low 64");
        }
        placeRepository.save(place);
    }

    public List<Place> getPlaces() {
        List<Place> places = placeRepository.findAll();
        places.sort(Comparator.comparing(Place::getNumber));
        return places;
    }

    public Place findById(Integer id) {
        return placeRepository.findById(id);
    }

    public Place findByNumber(Integer number) {
        List<Place> places = placeRepository.findAll();
        for (Place place: places) {
            if (Objects.equals(place.getNumber(), number)) {
                return place;
            }
        }
        return null;
    }

    public void delete(Place place) {
        placeRepository.delete(place.getId());
    }

    public List<Integer> getNumbers() {
        List<Place> places = placeRepository.findAll();
        List<Integer> numbers = new ArrayList<>();
        for (Place place: places) {
            numbers.add(place.getNumber());
        }
        return numbers;
    }
}
