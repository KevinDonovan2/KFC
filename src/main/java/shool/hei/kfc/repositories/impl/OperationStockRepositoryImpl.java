package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.OperationStock;
import hei.tantely.managementofrestaurantchain.entities.Stock;
import hei.tantely.managementofrestaurantchain.enums.OperationType;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.OperationStockRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;

@Repository
public class OperationStockRepositoryImpl implements OperationStockRepository {
    private final Connection connection;

    public OperationStockRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public OperationStock save(OperationStock toSave) {
        try {
            final var insertSql = "INSERT INTO operation_stock (type, operation_date, ingredient_id, stock_id, quantity) VALUES ( ?, ?, ?, ?, ?)";
            final var updateSql = "UPDATE operation_stock SET type = ?, operation_date = ?, ingredient_id = ?, stock_id = ?, quantity=? WHERE id = ?";
            final var query = toSave.getId() == null ? insertSql : updateSql;
            final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, toSave.getType().name());
            var date = toSave.getOperationDate() != null ? Timestamp.from(toSave.getOperationDate()): Timestamp.from(Instant.now());
            stmt.setTimestamp(2, date);

            if (toSave.getIngredient().getId() == null) {
                stmt.setNull(3, Types.BIGINT);
            } else {
                stmt.setInt(3, toSave.getIngredient().getId());
            }

            if (toSave.getStock().getId() == null) {
                stmt.setNull(4, Types.BIGINT);
            } else {
                stmt.setInt(4, toSave.getStock().getId());
            }

            stmt.setDouble(5, toSave.getQuantity());
            if (toSave.getId() != null) {
                stmt.setInt(6, toSave.getId());
            }
            final var rowsSaveIngredient = stmt.executeUpdate();
            if (rowsSaveIngredient == 0) {
                throw new InternalServerException("creating or updating a Operation stock...");
            }
            final var rs = stmt.getGeneratedKeys();
            if (toSave.getId() == null) {
                if (rs.next()) {
                    toSave.setId(rs.getInt(1));
                }
            }
            return toSave;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OperationStock findByIngredientId(Integer ingredientId) {
        final var query = "SELECT * FROM operation_stock WHERE ingredient_id ?";
        return getOperationStock(ingredientId, query);
    }

    @Override
    public OperationStock findByStockId(Integer stockId) {
        final var query = "SELECT * FROM operation_stock WHERE stock_id ?";
        return getOperationStock(stockId, query);
    }

    private OperationStock getOperationStock(Integer stockId, String query) {
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, stockId);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapRsToOperationStock(rs);
            }
            throw new NotFoundException("Operation of the stock menu not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    private OperationStock mapRsToOperationStock(ResultSet rs) throws SQLException {
        return OperationStock.builder()
                .id(rs.getInt("id"))
                .type(OperationType.valueOf(rs.getString("type")))
                .operationDate(rs.getTimestamp("operation_date").toInstant())
                .quantity(rs.getDouble("quantity"))
                .stock(Stock.builder().id(rs.getInt("stock_id")).build())
                .ingredient(Ingredient.builder().id(rs.getInt("ingredient_id")).build())
                .build();
    }
}
