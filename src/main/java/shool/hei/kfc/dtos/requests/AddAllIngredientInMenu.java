package shool.hei.kfc.dtos.requests;

import java.util.List;

public record AddAllIngredientInMenu(Integer menuId, List<IngredientWithQuantity> ingredients) {}
