package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.entities.Restaurant;

import java.util.List;


public interface RestaurantRepository {
    Restaurant save(Restaurant toSave);

    Restaurant findById(Integer id);

    List<Restaurant> findAll();
}
