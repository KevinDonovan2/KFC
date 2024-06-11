package shool.hei.kfc.services;

import hei.tantely.managementofrestaurantchain.dtos.requests.SupplyRequest;

public interface SupplyService {
    Double addStock(SupplyRequest supplyRequest);
}
