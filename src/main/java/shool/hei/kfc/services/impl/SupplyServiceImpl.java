package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.SupplyRequest;
import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.OperationStock;
import hei.tantely.managementofrestaurantchain.enums.OperationType;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.OperationStockRepository;
import hei.tantely.managementofrestaurantchain.repositories.StockRepository;
import hei.tantely.managementofrestaurantchain.services.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {
    private final StockRepository stockRepository;
    private final OperationStockRepository operationStockRepository;

    @Override
    public Double addStock(SupplyRequest supplyRequest) {
        var stock = stockRepository.findByIngredientIdAndRestaurantId(supplyRequest.ingredientId(), supplyRequest.restaurantId());
        if (stock == null){
            throw new NotFoundException("Stock not found");
        }
        var newQuantity = stock.getQuantity() + supplyRequest.quantity();
        stock.setQuantity(newQuantity);
        var stockModified = stockRepository.save(stock);
        var operationStock = OperationStock.builder()
                .quantity(supplyRequest.quantity())
                .stock(stock)
                .type(OperationType.ENTRY)
                .ingredient(Ingredient.builder().id(stock.getIngredient().getId()).build())
                .build();
        operationStockRepository.save(operationStock);
        return stockModified.getQuantity();
    }
}
