package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Menu;
import hei.tantely.managementofrestaurantchain.entities.Restaurant;
import hei.tantely.managementofrestaurantchain.entities.Sale;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.SaleRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.Instant;

@Repository
public class SaleRepositoryImpl implements SaleRepository {
    private final Connection connection;

    public SaleRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Sale save(Sale toSave) {
        final var insertQuery = "INSERT INTO sale (sale_price, sale_date, quantity,  restaurant_id, menu_id) VALUES (?, ?, ?, ?, ?)";
        final var updateQuery = "UPDATE sale SET  sale_price = ?, sale_date = ?, quantity = ?,  restaurant_id = ?, menu_id = ? WHERE id = ?";
        final var query = toSave.getId() != null ? updateQuery : insertQuery;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            var saleDate = toSave.getSaleDate() != null ? Timestamp.from(toSave.getSaleDate()): Timestamp.from(Instant.now());
            stmt.setDouble(1, toSave.getSalePrice());
            stmt.setTimestamp(2, saleDate);
            stmt.setInt(3, toSave.getQuantity());
            if (toSave.getRestaurant().getId() != null) {
                stmt.setInt(4, toSave.getRestaurant().getId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }

            if (toSave.getMenu().getId() != null) {
                stmt.setInt(5, toSave.getMenu().getId());
            } else {
                stmt.setNull(5, Types.BIGINT);
            }


            if (toSave.getId() != null) {
                stmt.setInt(6, toSave.getId());
            }
            final var rowStock = stmt.executeUpdate();
            if (rowStock == 0) {
                throw new InternalServerException("mistake when creating a sale");
            }
            try (final var rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    toSave.setId(rs.getInt(1));
                }
            }
            return toSave;

        } catch (SQLException e) {
            throw new InternalServerException("mistake when creating a sale");
        }
    }

    @Override
    public Sale findById(Integer id) {
        try (final var stmt = connection.prepareStatement("SELECT * FROM sale WHERE id = ?")) {
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


    private Sale mapRsToStock(ResultSet rs) throws SQLException {
        return Sale.builder()
                .id(rs.getInt("id"))
                .quantity(rs.getInt("quantity"))
                .restaurant(Restaurant.builder().id(rs.getInt("restaurant_id")).build())
                .menu(Menu.builder().id(rs.getInt("menu_id")).build())
                .salePrice(rs.getInt("sale_price"))
                .saleDate(rs.getDate("sale_date").toInstant())
                .build();
    }


}
