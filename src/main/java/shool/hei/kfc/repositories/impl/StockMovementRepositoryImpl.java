package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.dtos.responses.MovementStock;
import hei.tantely.managementofrestaurantchain.dtos.responses.TopXUsedIngredient;
import hei.tantely.managementofrestaurantchain.enums.OperationType;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockMovementRepositoryImpl implements StockMovementRepository {
    private final Connection connection;

    @Override
    public List<MovementStock> findAllDetailMovements(Integer restaurantId, Instant startDate, Instant endDate) {
        var query = """
                SELECT
                     os.operation_date AS movementDate, i.name AS ingredientName, os.type AS movementType, s.quantity AS remainingQuantity, u.name AS unitName
                FROM stock AS s
                   INNER JOIN operation_stock AS os ON s.id = os.stock_id
                   INNER JOIN ingredient AS i ON os.ingredient_id = i.id
                   INNER JOIN unit AS u ON u.id = i.unit_id
                WHERE s.restaurant_id = ? AND os.operation_date  >= ? AND os.operation_date  <= ?
                """;
        var stocksDetails = new ArrayList<MovementStock>();
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            stmt.setTimestamp(2, Timestamp.from(startDate));
            stmt.setTimestamp(3, Timestamp.from(endDate));
            final var rs = stmt.executeQuery();
            while (rs.next())
                stocksDetails.add(this.mapRsToIngredientMenu(rs));

            return stocksDetails;
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<TopXUsedIngredient> findTopXUsedIngredients(Integer restaurantId, Instant instantStart, Instant instantEnd, Integer limit) {
        var query = """
                WITH top_menus AS (
                    SELECT sale.menu_id, menu.name,
                           SUM(sale.quantity) AS total_quantity
                    FROM sale
                    INNER JOIN menu ON menu.id = sale.menu_id
                    WHERE sale.restaurant_id = ?
                    AND sale.sale_date BETWEEN ? AND ?
                    GROUP BY sale.menu_id, menu.name
                ),
                ingredient_usage AS (
                    SELECT im.ingredient_id, tm.name,  SUM(im.quantity * tm.total_quantity) AS total_usage
                    FROM ingredient_menu im
                    JOIN top_menus tm ON im.menu_id = tm.menu_id
                    GROUP BY im.ingredient_id, tm.name
                    ORDER BY total_usage DESC
                    LIMIT ?
                )
                SELECT i.id AS ingredient_id, i.name AS ingredient_name, u.name AS unit_name, iu.name AS menu_name, iu.total_usage
                FROM ingredient_usage iu
                JOIN ingredient i ON iu.ingredient_id = i.id
                JOIN unit u ON i.unit_id = u.id;
                """;
        var topXUsedIngredients = new ArrayList<TopXUsedIngredient>();
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, restaurantId);
            stmt.setTimestamp(2, Timestamp.from(instantStart));
            stmt.setTimestamp(3, Timestamp.from(instantEnd));
            stmt.setInt(4, limit);
            final var rs = stmt.executeQuery();
            while (rs.next())
                topXUsedIngredients.add(this.mapRsToTopXUsedIngredient(rs));

            return topXUsedIngredients;
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private MovementStock mapRsToIngredientMenu(ResultSet rs) throws SQLException {
        var movementDate = rs.getTimestamp("movementDate").toInstant();
        var zoneId = ZoneId.of(ZoneId.systemDefault().getId());
        var zonedDateTime = movementDate.atZone(zoneId);
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        var formattedDateTime = zonedDateTime.format(formatter);
        return new MovementStock(
                formattedDateTime,
                rs.getString("ingredientName"),
                OperationType.valueOf(rs.getString("movementType")),
                rs.getDouble("remainingQuantity"),
                rs.getString("unitName")

        );
    }

    private TopXUsedIngredient mapRsToTopXUsedIngredient(ResultSet rs) throws SQLException {
        return new TopXUsedIngredient(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getString("menu_name"),
                rs.getDouble("total_usage"),
                rs.getString("unit_name")
        );
    }
}
