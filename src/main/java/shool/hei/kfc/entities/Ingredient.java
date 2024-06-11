package shool.hei.kfc.entities;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class Ingredient {
    private Integer id;
    private Integer price;
    private String name;
    private Unit unit;
    private List<Stock> stocks = new ArrayList<>();
    private List<OperationStock> operationStocks = new ArrayList<>();
    private List<IngredientMenu> ingredientMenus = new ArrayList<>();
}
