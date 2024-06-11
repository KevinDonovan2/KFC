package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.dtos.requests.AddAllIngredientInMenu;
import hei.tantely.managementofrestaurantchain.entities.Stock;

import java.util.List;

public interface StockRepository {
    Stock save(Stock toSave);

    Stock findById(Integer id);

    Stock findByIngredientId(Integer id);

    Stock findByIngredientIdAndRestaurantId(Integer ingredientId, Integer restaurantId);

    boolean hasStockInsufficient(AddAllIngredientInMenu addMenuRequest);

    List<Stock> findAllByRestaurantId(Integer restaurantId);
}
