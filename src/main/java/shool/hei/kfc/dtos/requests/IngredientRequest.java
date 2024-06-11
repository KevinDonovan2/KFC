package shool.hei.kfc.dtos.requests;

public record IngredientRequest(
        String ingredientName,
        Integer ingredientPrice,
        Integer unitId,
        String unitName
) {}
