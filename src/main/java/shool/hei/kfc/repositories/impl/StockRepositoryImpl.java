package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.AddAllIngredientInMenu;
import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.Restaurant;
import hei.tantely.managementofrestaurantchain.entities.Stock;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.StockRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StockRepositoryImpl implements StockRepository {
    private final Connection connection;

    public StockRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stock save(Stock toSave) {
        final var insertQuery = "INSERT INTO stock (quantity, ingredient_id, restaurant_id) VALUES (?, ?, ?)";
        final var updateQuery = "UPDATE stock SET  quantity = ?, ingredient_id = ?, restaurant_id = ? WHERE id = ?";
        final var query = toSave.getId() != null ? updateQuery : insertQuery;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDouble(1, toSave.getQuantity());
            if (toSave.getIngredient().getId() != null) {
                stmt.setInt(2, toSave.getIngredient().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }

            if (toSave.getRestaurant().getId() != null) {
                stmt.setInt(3, toSave.getRestaurant().getId());
            } else {
                stmt.setNull(3, Types.BIGINT);
            }


            if (toSave.getId() != null) {
                stmt.setInt(4, toSave.getId());
            }
            final var rowStock = stmt.executeUpdate();
            if (rowStock == 0) {
                throw new InternalServerException("mistake when creating a stock");
            }
            try (final var rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    toSave.setId(rs.getInt(1));
                }
            }
            return toSave;

        } catch (SQLException e) {
            throw new InternalServerException("mistake when creating a Stock");
        }
    }

    @Override
    public Stock findById(Integer id) {
        final var query = "SELECT * FROM stock WHERE id = ?";
        return getStock(id, query);
    }

    @Override
    public Stock findByIngredientId(Integer id) {
        final var query = "SELECT * FROM stock WHERE ingredient_id = ?";
        return getStock(id, query);
    }

    @Override
    public Stock findByIngredientIdAndRestaurantId(Integer ingredientId, Integer restaurantId) {
        var query = "SELECT * FROM stock WHERE ingredient_id = ? AND restaurant_id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ingredientId);
            stmt.setInt(2, restaurantId);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapRsToStock(rs);
            }
            throw new NotFoundException("Stock not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public boolean hasStockInsufficient(AddAllIngredientInMenu addAllIngredientInMenu) {
        try (final var stmt = connection.prepareStatement("SELECT * FROM stock");
             final var rs = stmt.executeQuery()) {
            while (rs.next()){
                var quantity = rs.getDouble("quantity");
                for (var q: addAllIngredientInMenu.ingredients()){
                    if (quantity < q.quantity()){
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Stock> findAllByRestaurantId(Integer restaurantId) {
        var stocks = new ArrayList<Stock>();
        var query = "SELECT * FROM stock WHERE restaurant_id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            try (final var rs = stmt.executeQuery()) {
                while (rs.next())
                    stocks.add(mapRsToStock(rs));
            }
            return stocks;
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private Stock getStock(Integer id, String query) {
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapRsToStock(rs);
            }
            throw new NotFoundException("Stock not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    private Stock mapRsToStock(ResultSet rs) throws SQLException {
        return Stock.builder()
                .id(rs.getInt("id"))
                .ingredient(Ingredient.builder().id(rs.getInt("ingredient_id")).build())
                .quantity(rs.getDouble("quantity"))
                .restaurant(Restaurant.builder().id(rs.getInt("restaurant_id")).build())
                .build();
    }


}
