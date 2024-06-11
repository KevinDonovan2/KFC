package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.dtos.responses.MovementStock;
import hei.tantely.managementofrestaurantchain.dtos.responses.TopXUsedIngredient;

import java.time.Instant;
import java.util.List;

public interface StockMovementRepository {
    List<MovementStock> findAllDetailMovements(Integer restaurantId, Instant startDate, Instant endDate);

    List<TopXUsedIngredient> findTopXUsedIngredients(Integer restaurantId, Instant instantStart, Instant instantEnd, Integer limit);
}
