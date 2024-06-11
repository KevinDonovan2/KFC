package shool.hei.kfc.entities;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class IngredientMenu {
    private Integer id;
    private Double quantity;
    private Menu menu;
    private Ingredient ingredient;
}
