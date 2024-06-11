package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.entities.IngredientMenu;

import java.util.List;


public interface IngredientMenuRepository {
    IngredientMenu save(IngredientMenu toSave);

    IngredientMenu findByMenuId(Integer id);

    IngredientMenu findByIngredientId(Integer id);

    IngredientMenu findByIngredientIdAndMenuId(Integer ingredientId, Integer menuId);

    void deleteByIngredientIdAndMenuId(IngredientMenu ingredientMenu);

    List<IngredientMenu> findAllByMenuId(Integer menuId);
}
