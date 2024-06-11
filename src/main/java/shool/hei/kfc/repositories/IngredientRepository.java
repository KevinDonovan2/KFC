package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.entities.Ingredient;

import java.util.List;


public interface IngredientRepository {
    Ingredient save(Ingredient toSave);

    Ingredient findById(Integer id);

    Ingredient findByUnitId(Integer unitId);

    List<Ingredient> findAll();
}
