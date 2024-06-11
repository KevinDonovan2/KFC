package shool.hei.kfc.services;

import hei.tantely.managementofrestaurantchain.dtos.requests.IntervalDate;
import hei.tantely.managementofrestaurantchain.dtos.responses.MovementStock;
import hei.tantely.managementofrestaurantchain.dtos.responses.TopXUsedIngredient;

import java.util.List;

public interface StockMovementService {


    List<MovementStock> findAllDetailMovements(Integer restaurantId, IntervalDate intervalDate);

    List<TopXUsedIngredient> findTopXUsedIngredients(Integer restaurantId, IntervalDate intervalDate, Integer limit);
}
