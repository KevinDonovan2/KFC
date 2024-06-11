package shool.hei.kfc.dtos.requests;

public record SaveOneIngredientInMenu(
        Integer menuId,
        Integer ingredientId,
        Double quantity
){}
