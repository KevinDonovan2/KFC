package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.IngredientRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.IngredientResponse;
import hei.tantely.managementofrestaurantchain.dtos.responses.UnitResponse;
import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.Stock;
import hei.tantely.managementofrestaurantchain.entities.Unit;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.IngredientRepository;
import hei.tantely.managementofrestaurantchain.repositories.RestaurantRepository;
import hei.tantely.managementofrestaurantchain.repositories.StockRepository;
import hei.tantely.managementofrestaurantchain.repositories.UnitRepository;
import hei.tantely.managementofrestaurantchain.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final RestaurantRepository restaurantRepository;
    private final StockRepository stockRepository;


    @Override
    public IngredientResponse createIngredient(IngredientRequest ingredientRequest) {
        var toCreate = Ingredient.builder()
                .price(ingredientRequest.ingredientPrice())
                .name(ingredientRequest.ingredientName())
                .build();
        if (ingredientRequest.unitId() == null) {
            var unit = Unit.builder()
                    .name(ingredientRequest.unitName())
                    .build();
            var created = unitRepository.save(unit);
            toCreate.setUnit(created);
        } else {
            var unit = unitRepository.findById(ingredientRequest.unitId());
            if (unit == null) {
                throw new NotFoundException("Unit not found");
            }
            toCreate.setUnit(unit);
        }
        var createdIngredient = ingredientRepository.save(toCreate);
        var restaurants = restaurantRepository.findAll();
        for (var restaurant : restaurants) {
            var stock = Stock.builder()
                    .restaurant(restaurant)
                    .ingredient(createdIngredient)
                    .quantity(0.0)
                    .build();
            stockRepository.save(stock);
        }
        return new IngredientResponse(
                createdIngredient.getId(),
                createdIngredient.getName(),
                createdIngredient.getPrice(),
                new UnitResponse(
                        createdIngredient.getUnit().getId(),
                        createdIngredient.getUnit().getName()
                )
        );
    }
}
