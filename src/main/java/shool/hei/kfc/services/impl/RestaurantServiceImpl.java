package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.RestaurantRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.RestaurantResponse;
import hei.tantely.managementofrestaurantchain.entities.Restaurant;
import hei.tantely.managementofrestaurantchain.entities.Stock;
import hei.tantely.managementofrestaurantchain.repositories.IngredientRepository;
import hei.tantely.managementofrestaurantchain.repositories.RestaurantRepository;
import hei.tantely.managementofrestaurantchain.repositories.StockRepository;
import hei.tantely.managementofrestaurantchain.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final IngredientRepository ingredientRepository;
    private final StockRepository stockRepository;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        var ingredients = ingredientRepository.findAll();
        var toCreate = Restaurant.builder().address(restaurantRequest.address()).build();
        var toCreated = restaurantRepository.save(toCreate);
        if (!ingredients.isEmpty()) {
            for (var ingredient : ingredients) {
                var stock = Stock.builder()
                        .restaurant(toCreated)
                        .ingredient(ingredient)
                        .quantity(0.0)
                        .build();
                stockRepository.save(stock);
            }
        }
        return new RestaurantResponse(toCreated.getId(), toCreated.getAddress());
    }

}
