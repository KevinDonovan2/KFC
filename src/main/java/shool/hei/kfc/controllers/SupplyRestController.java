package shool.hei.kfc.controllers;

import hei.tantely.managementofrestaurantchain.dtos.requests.SupplyRequest;
import hei.tantely.managementofrestaurantchain.services.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class SupplyRestController {
    private final SupplyService supplyService;

    @PutMapping("/stocks/add")
    public Double addStock(@RequestBody SupplyRequest supplyRequest){
        return supplyService.addStock(supplyRequest);
    }

}
