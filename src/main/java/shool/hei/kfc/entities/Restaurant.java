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
public class Restaurant {
    private Integer id;
    private String address;
    private List<Sale> sales = new ArrayList<>();
    private List<Stock> stocks = new ArrayList<>();
}
