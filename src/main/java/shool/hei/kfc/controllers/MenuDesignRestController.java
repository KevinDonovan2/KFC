package shool.hei.kfc.controllers;

import hei.tantely.managementofrestaurantchain.dtos.requests.AddAllIngredientInMenu;
import hei.tantely.managementofrestaurantchain.dtos.requests.SaveOneIngredientInMenu;
import hei.tantely.managementofrestaurantchain.services.MenuDesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/design/menu")
public class MenuDesignRestController {
   private final MenuDesignService menuDesignService;

   @PostMapping("/add/all")
   @ResponseStatus(HttpStatus.CREATED)
   public void addAllIngredients(@RequestBody AddAllIngredientInMenu addAllIngredientInMenu){
       menuDesignService.addAllIngredientsInMenu(addAllIngredientInMenu);
   }

   @DeleteMapping("/{menuId}/ingredient/{ingredientId}")
   public void deleteIngredientInMenu(@PathVariable Integer menuId, @PathVariable Integer ingredientId){
      menuDesignService.deleteIngredientInMenu(menuId, ingredientId);
   }

   @PostMapping("/attach")
   public void attachIngredientInMenu(@RequestBody SaveOneIngredientInMenu saveOneIngredientInMenu){
      menuDesignService.attachIngredientInMenu(saveOneIngredientInMenu);
   }

   @PutMapping("/modify")
   public void modifyQuantityIngredientInMenu(@RequestBody SaveOneIngredientInMenu saveOneIngredientInMenu){
      menuDesignService.modifyQuantityIngredientInMenu(saveOneIngredientInMenu);
   }

}
