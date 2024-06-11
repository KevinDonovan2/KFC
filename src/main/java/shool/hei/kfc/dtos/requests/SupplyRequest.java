package shool.hei.kfc.dtos.requests;

public record SupplyRequest (
        Integer restaurantId,
        Double quantity,
        Integer ingredientId
) {}
