package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.Unit;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepository {
    private final Connection connection;

    @Override
    public Ingredient save(Ingredient toSave) {
        final var insertSql = "INSERT INTO ingredient (price, name, unit_id) VALUES ( ?, ?, ?)";
        final var updateSql = "UPDATE ingredient SET price = ?,  name = ?, unit_id = ? WHERE id = ?";
        final var query = toSave.getId() == null ? insertSql : updateSql;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, toSave.getPrice());
            stmt.setString(2, toSave.getName());
            if (toSave.getUnit().getId() == null) {
                stmt.setNull(3, Types.BIGINT);
            } else {
                stmt.setInt(3, toSave.getUnit().getId());
            }
            if (toSave.getId() != null) {
                stmt.setInt(4, toSave.getId());
            }
            final var rowsSaveIngredient = stmt.executeUpdate();
            if (rowsSaveIngredient == 0) {
                throw new InternalServerException("creating or updating a ingredient...");
            }
            try (final var rs = stmt.getGeneratedKeys()) {
                if (toSave.getId() == null) {
                    if (rs.next()) {
                        toSave.setId(rs.getInt(1));
                    }
                }
            }
            return toSave;
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public Ingredient findById(Integer id) {
        final var query = "SELECT * FROM ingredient WHERE id = ?";
        return getIngredient(id, query);
    }

    @Override
    public Ingredient findByUnitId(Integer unitId) {
        final var query = "SELECT * FROM ingredient WHERE unit_id = ?";
        return getIngredient(unitId, query);
    }

    @Override
    public List<Ingredient> findAll() {
        var ingredients = new ArrayList<Ingredient>();
        final var query = "SELECT * FROM ingredient";
        try (final var stmt = connection.prepareStatement(query)) {
            try (final var rs = stmt.executeQuery()) {
                while (rs.next())
                    ingredients.add(mapRsToIngredient(rs));
            }
            return ingredients;
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private Ingredient getIngredient(Integer id, String query) {
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapRsToIngredient(rs);
            }
            throw new NotFoundException("Ingredient not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    private Ingredient mapRsToIngredient(ResultSet rs) throws SQLException {
        return Ingredient.builder()
                .id(rs.getInt("id"))
                .price(rs.getInt("price"))
                .name(rs.getString("name"))
                .unit(Unit.builder().id(rs.getInt("unit_id")).build())
                .build();
    }
}
