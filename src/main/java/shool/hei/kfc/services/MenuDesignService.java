package shool.hei.kfc.services;

import hei.tantely.managementofrestaurantchain.dtos.requests.AddAllIngredientInMenu;
import hei.tantely.managementofrestaurantchain.dtos.requests.SaveOneIngredientInMenu;

public interface MenuDesignService {
    void addAllIngredientsInMenu(AddAllIngredientInMenu addAllIngredientInMenu);

    void modifyQuantityIngredientInMenu(SaveOneIngredientInMenu saveOneIngredientInMenu);

    void attachIngredientInMenu(SaveOneIngredientInMenu saveOneIngredientInMenu);

    void deleteIngredientInMenu(Integer menuId, Integer ingredientId);
}
