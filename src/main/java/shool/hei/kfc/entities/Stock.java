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
public class Stock {
    private Integer id;
    private Double quantity;
    private Ingredient ingredient;
    private Restaurant restaurant;
    private List<OperationStock> operationStocks = new ArrayList<>();
}
