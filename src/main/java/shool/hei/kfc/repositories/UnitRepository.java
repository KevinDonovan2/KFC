package shool.hei.kfc.repositories;

import hei.tantely.managementofrestaurantchain.entities.Unit;

import java.util.List;

public interface UnitRepository {
    Unit save(Unit toSave);

    Unit findById(Integer id);

    List<Unit> findAllById(List<Integer> unitIds);
}
