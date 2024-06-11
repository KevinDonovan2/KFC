package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.AddAllIngredientInMenu;
import hei.tantely.managementofrestaurantchain.dtos.requests.SaveOneIngredientInMenu;
import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.IngredientMenu;
import hei.tantely.managementofrestaurantchain.exceptions.BadRequestException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.IngredientMenuRepository;
import hei.tantely.managementofrestaurantchain.repositories.IngredientRepository;
import hei.tantely.managementofrestaurantchain.repositories.MenuRepository;
import hei.tantely.managementofrestaurantchain.services.MenuDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuDesignServiceImpl implements MenuDesignService {
    private final MenuRepository  menuRepository;
    private final IngredientMenuRepository ingredientMenuRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public void addAllIngredientsInMenu(AddAllIngredientInMenu addAllIngredientInMenu) {
        var menu = menuRepository.findById(addAllIngredientInMenu.menuId());
        if (menu == null){
            throw new BadRequestException("Menu not found");
        }
        for (var ingredientIdWithQuantity : addAllIngredientInMenu.ingredients()){
            var ingredientMenu = IngredientMenu.builder()
                    .menu(menu)
                    .ingredient(Ingredient.builder().id(ingredientIdWithQuantity.id()).build())
                    .quantity(ingredientIdWithQuantity.quantity())
                    .build();
            ingredientMenuRepository.save(ingredientMenu);
        }
    }

    @Override
    public void modifyQuantityIngredientInMenu(SaveOneIngredientInMenu saveOneIngredientInMenu) {
        var ingredientMenu = ingredientMenuRepository.findByIngredientIdAndMenuId(saveOneIngredientInMenu.ingredientId(), saveOneIngredientInMenu.menuId());
        if (ingredientMenu == null){
            throw new NotFoundException("Ingredient menu not found");
        }
        ingredientMenuRepository.save(ingredientMenu);
    }

    @Override
    public void attachIngredientInMenu(SaveOneIngredientInMenu saveOneIngredientInMenu) {
      var menu = menuRepository.findById(saveOneIngredientInMenu.menuId());
      if (menu == null){
          throw new NotFoundException("Menu not found");
      }
      var ingredient = ingredientRepository.findById(saveOneIngredientInMenu.ingredientId());
      if (ingredient == null){
          throw new NotFoundException("Ingredient not found");
      }
      var ingredientMenu = IngredientMenu.builder()
              .quantity(saveOneIngredientInMenu.quantity())
              .ingredient(ingredient)
              .menu(menu)
              .build();
      ingredientMenuRepository.save(ingredientMenu);
    }

    @Override
    public void deleteIngredientInMenu(Integer menuId, Integer ingredientId) {
        var ingredientMenu = ingredientMenuRepository.findByIngredientIdAndMenuId(ingredientId, ingredientId);
        if (ingredientMenu == null){
            throw new NotFoundException("Ingredient not found ");
        }
        ingredientMenuRepository.deleteByIngredientIdAndMenuId(ingredientMenu);
    }
}
