package shool.hei.kfc.dtos.responses;

import hei.tantely.managementofrestaurantchain.enums.OperationType;

public record MovementStock(
        String movementDate,
        String ingredientName,
        OperationType movementType,
        Double remainingQuantity,
        String unitName
) {}
