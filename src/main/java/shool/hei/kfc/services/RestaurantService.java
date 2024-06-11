package shool.hei.kfc.services;

import hei.tantely.managementofrestaurantchain.dtos.requests.RestaurantRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.RestaurantResponse;

public interface RestaurantService {
    RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest);
}
