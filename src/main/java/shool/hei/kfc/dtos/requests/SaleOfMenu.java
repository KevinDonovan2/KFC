package shool.hei.kfc.dtos.requests;

public record SaleOfMenu (
   Integer restaurantId,
   Integer menuId,
   Integer quantity
) {}
