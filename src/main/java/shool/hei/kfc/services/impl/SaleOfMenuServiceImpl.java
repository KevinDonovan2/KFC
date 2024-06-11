package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.SaleOfMenu;
import hei.tantely.managementofrestaurantchain.entities.*;
import hei.tantely.managementofrestaurantchain.enums.OperationType;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.*;
import hei.tantely.managementofrestaurantchain.services.SaleOfMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SaleOfMenuServiceImpl implements SaleOfMenuService {
    private final MenuRepository menuRepository;
    private final StockRepository stockRepository;
    private final OperationStockRepository operationStockRepository;
    private final SaleRepository saleRepository;
    private final IngredientMenuRepository ingredientMenuRepository;
    @Override
    public Double saleOfMenu(SaleOfMenu saleOfMenu) {
        var menu = menuRepository.findById(saleOfMenu.menuId());
        if (menu == null){
            throw new NotFoundException("Menu not found ");
        }
        var ingredientMenus = ingredientMenuRepository.findAllByMenuId(saleOfMenu.menuId());
        var stocksOneRestaurant = stockRepository.findAllByRestaurantId(saleOfMenu.restaurantId());
        if (!isValidSaleOfManu(ingredientMenus, stocksOneRestaurant, saleOfMenu.quantity())){
            throw new NotFoundException("only one ingredient is not enough");
        }
        for (var ingredientMenu : ingredientMenus) {
            for (var stock : stocksOneRestaurant) {
                if (Objects.equals(stock.getIngredient().getId(), ingredientMenu.getIngredient().getId())){
                    var newQuantity = stock.getQuantity() - (ingredientMenu.getQuantity() * saleOfMenu.quantity());
                    stock.setQuantity(newQuantity);
                    var operationStock = OperationStock.builder()
                            .quantity(ingredientMenu.getQuantity() * saleOfMenu.quantity())
                            .stock(stock)
                            .type(OperationType.SORTIE)
                            .ingredient(Ingredient.builder().id(ingredientMenu.getIngredient().getId()).build())
                            .build();
                    stockRepository.save(stock);
                    operationStockRepository.save(operationStock);
                }
            }
        }
        var sale = Sale.builder()
                .saleDate(Instant.now())
                .salePrice(menu.getCurrentPrice())
                .quantity(saleOfMenu.quantity())
                .menu(menu)
                .restaurant(Restaurant.builder().id(saleOfMenu.restaurantId()).build())
                .build();
        var soled = saleRepository.save(sale);
        return (double) (soled.getSalePrice() * saleOfMenu.quantity());
    }

    private boolean isValidSaleOfManu(List<IngredientMenu> ingredientMenus, List<Stock> stocksOneRestaurant, Integer quantity) {
        for (var ingredientMenu : ingredientMenus) {
            for (var stock : stocksOneRestaurant) {
                if (Objects.equals(stock.getIngredient().getId(), ingredientMenu.getIngredient().getId())){
                    if (stock.getQuantity() < (ingredientMenu.getQuantity() * quantity)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
