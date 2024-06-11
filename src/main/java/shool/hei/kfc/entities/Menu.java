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
public class Menu {
    private Integer id;
    private String name;
    private Integer currentPrice;
    private List<Sale> sales = new ArrayList<>();
    private Ingredient ingredient;
    private List<IngredientMenu> ingredientMenus = new ArrayList<>();
}
