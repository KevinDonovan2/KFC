package shool.hei.kfc.controllers;

import hei.tantely.managementofrestaurantchain.dtos.requests.RestaurantRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.RestaurantResponse;
import hei.tantely.managementofrestaurantchain.services.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/restaurants")
public class RestaurantRestController {

    private final RestaurantService restaurantService;

    public RestaurantRestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse createRestaurant(@RequestBody RestaurantRequest restaurantRequest) {
        return restaurantService.createRestaurant(restaurantRequest);
    }
}
