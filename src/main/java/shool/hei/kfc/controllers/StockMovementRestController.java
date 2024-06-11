package shool.hei.kfc.controllers;

import hei.tantely.managementofrestaurantchain.dtos.requests.IntervalDate;
import hei.tantely.managementofrestaurantchain.dtos.responses.MovementStock;
import hei.tantely.managementofrestaurantchain.dtos.responses.TopXUsedIngredient;
import hei.tantely.managementofrestaurantchain.services.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/movements")
@RequiredArgsConstructor
public class StockMovementRestController {
    private final StockMovementService stockMovementService;

    @GetMapping("/details/{restaurantId}/limit/{limit}")
    public List<TopXUsedIngredient> getTopXUsedIngredients(@PathVariable Integer restaurantId, @RequestBody IntervalDate intervalDate, @PathVariable Integer limit){
        return stockMovementService.findTopXUsedIngredients(restaurantId, intervalDate, limit);
    }


    @GetMapping("/details/{restaurantId}")
    public List<MovementStock> getAllDetailMovements(@PathVariable Integer restaurantId, @RequestBody IntervalDate intervalDate){
        return stockMovementService.findAllDetailMovements(restaurantId, intervalDate);
    }


}
