package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Unit;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.UnitRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UnitRepositoryImpl implements UnitRepository {
    private final Connection connection;

    public UnitRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Unit save(Unit toSave) {
        final var insertQuery = "INSERT INTO unit (name) VALUES (?)";
        final var updateQuery = "UPDATE unit SET  name = ? WHERE id = ?";
        final var query = toSave.getId() != null ? updateQuery : insertQuery;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, toSave.getName());
            if (toSave.getId() != null) {
                stmt.setInt(2, toSave.getId());
            }
            final var rowsUnit = stmt.executeUpdate();
            if (rowsUnit == 0) {
                throw new InternalServerException("mistake when creating a unit");
            }
            try (final var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    toSave.setId(rs.getInt(1));
                }
            }
            return toSave;

        } catch (SQLException e) {
            throw new InternalServerException("mistake when creating a unit");
        }
    }

    @Override
    public Unit findById(Integer id) {
        final var query = "SELECT * FROM unit WHERE id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRsToUnit(rs);
                }
            }
            throw new NotFoundException("mistake when creating a unit");
        } catch (SQLException e) {
            throw new NotFoundException("mistake when creating a unit");
        }
    }

    @Override
    public List<Unit> findAllById(List<Integer> unitIds) {
        var queryBuilder = new StringBuilder("SELECT * FROM unit WHERE id IN (");
        for (int i = 0; i < unitIds.size(); i++) {
            queryBuilder.append("?");
            if (i < unitIds.size() - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");
        try (final var stmt = connection.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < unitIds.size(); i++) {
                stmt.setInt(i + 1, unitIds.get(i));
            }
            try (final var rs = stmt.executeQuery()) {
                var units = new ArrayList<Unit>();
                while (rs.next()) {
                    units.add(mapRsToUnit(rs));
                }
                return units;
            }
        } catch (SQLException e) {
            throw new InternalServerException("Error while fetching units by IDs");
        }
    }

    private Unit mapRsToUnit(ResultSet rs) throws SQLException {
        return Unit.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
