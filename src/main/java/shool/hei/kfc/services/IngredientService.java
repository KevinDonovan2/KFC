package shool.hei.kfc.services;

import hei.tantely.managementofrestaurantchain.dtos.requests.IngredientRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.IngredientResponse;

public interface IngredientService {
    IngredientResponse createIngredient(IngredientRequest ingredientRequest);
}
