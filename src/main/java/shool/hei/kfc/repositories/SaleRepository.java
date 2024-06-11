package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.entities.Sale;


public interface SaleRepository {
    Sale save(Sale toSave);

    Sale findById(Integer id);

}
