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
public class Unit {
    private Integer id;
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();
}
