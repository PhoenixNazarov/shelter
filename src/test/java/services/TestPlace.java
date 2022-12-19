package services;

import shelter.database.models.Category;
import shelter.database.models.Place;
import shelter.database.service.CategoryService;
import shelter.database.service.PlaceService;

import java.util.List;

public class TestPlace {
    public static void main(String[] args) {
        PlaceService placeService = new PlaceService();
        List<Place> places = placeService.getPlaces();

        Place place = new Place();
        place.setNumber(places.size());
        place.setDescription("Комната 123");
        place.setMaxAnimal(421412);

        placeService.createPlace(place);

        List<Place> places2 = placeService.getPlaces();

        if (places.size()+1 != places2.size()) {
            throw new RuntimeException();
        }

        if (!places2.get(places2.size() - 1).getId().equals(place.getId())) {
            throw new RuntimeException();
        }
        if (!places2.get(places2.size() - 1).getDescription().equals(place.getDescription())) {
            throw new RuntimeException();
        }
        if (!places2.get(places2.size() - 1).getMaxAnimal().equals(place.getMaxAnimal())) {
            throw new RuntimeException();
        }

    }
}
