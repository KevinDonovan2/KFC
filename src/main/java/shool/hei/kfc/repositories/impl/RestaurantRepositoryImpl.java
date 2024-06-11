package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Restaurant;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.RestaurantRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final Connection connection;

    public RestaurantRepositoryImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Restaurant save(Restaurant toSave) {
        final var insertQuery = "INSERT INTO restaurant (address) VALUES (?)";
        final var updateQuery = "UPDATE restaurant SET  address = ? WHERE id = ?";
        final var query = toSave.getId() != null ? updateQuery : insertQuery;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, toSave.getAddress());
            if (toSave.getId() != null) {
                stmt.setInt(2, toSave.getId());
            }
            final var rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new InternalServerException("mistake when creating a restaurant");
            }
            try (final var rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    toSave.setId(rs.getInt(1));
                }
            }
            return toSave;

        } catch (SQLException e) {
            throw new InternalServerException("mistake when creating a restaurant");
        }

    }

    @Override
    public Restaurant findById(Integer id) {
        final var query = "SELECT * FROM restaurant WHERE id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRsToRestaurant(rs);
                }
            }
            throw new NotFoundException("mistake when creating a restaurant");
        } catch (SQLException e) {
            throw new NotFoundException("mistake when creating a restaurant");
        }
    }

    @Override
    public List<Restaurant> findAll() {
        final var query = "SELECT * FROM restaurant";
        final var restaurants = new ArrayList<Restaurant>();
        try (final var stmt = connection.prepareStatement(query)) {
            try (final var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(mapRsToRestaurant(rs));
                }
            }
            return restaurants;
        } catch (SQLException e) {
            throw new NotFoundException("mistake when creating a restaurant");
        }
    }


    private Restaurant mapRsToRestaurant(ResultSet rs) throws SQLException {
        return (new Restaurant())
                .setId(rs.getInt("id"))
                .setAddress(rs.getString("address"));
    }
}
