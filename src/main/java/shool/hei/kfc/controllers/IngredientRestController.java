package shool.hei.kfc.controllers;

import hei.tantely.managementofrestaurantchain.dtos.requests.IngredientRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.IngredientResponse;
import hei.tantely.managementofrestaurantchain.services.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/ingredients")
public class IngredientRestController {
    private final IngredientService ingredientService;

    public IngredientRestController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(@RequestBody IngredientRequest ingredientRequest){
        return ingredientService.createIngredient(ingredientRequest);
    }
}
