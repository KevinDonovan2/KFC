package shool.hei.kfc.entities;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class Sale {
    private Integer id;
    private Integer salePrice;
    private Instant saleDate;
    private Integer quantity;
    private Restaurant restaurant;
    private Menu menu;
}
