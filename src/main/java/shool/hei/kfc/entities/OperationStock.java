package shool.hei.kfc.entities;

import hei.tantely.managementofrestaurantchain.enums.OperationType;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class OperationStock {
    private Integer id;
    private OperationType type;
    private Double quantity;
    private Instant operationDate;
    private Ingredient ingredient;
    private Stock stock;
}
