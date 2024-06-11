package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Menu;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.MenuRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Repository
public class MenuRepositoryImpl implements MenuRepository {
    private final Connection connection;

    public MenuRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Menu save(Menu toSave) {
        final var insertSql = "INSERT INTO menu (name, current_price) VALUES ( ?,  ?)";
        final var updateSql = "UPDATE menu SET name = ?, current_price = ? WHERE id = ?";
        final var query = toSave.getId() == null ? insertSql : updateSql;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, toSave.getName());
            stmt.setInt(2, toSave.getCurrentPrice());

            if (toSave.getId() != null) {
                stmt.setInt(3, toSave.getId());
            }

            final var rowsSaveMenu = stmt.executeUpdate();
            if (rowsSaveMenu == 0) {
                throw new InternalServerException("creating or updating a Menu...");
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public Menu findById(Integer id) {
        final var query = "SELECT * FROM menu WHERE id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapRsToMenu(rs);
            }
            throw new NotFoundException("Menu not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Menu> findAll() {
        final var query = "SELECT * FROM menu";
        final var menus = new ArrayList<Menu>();
        try (final var stmt = connection.prepareStatement(query)) {
            try (final var rs = stmt.executeQuery()) {
                while (rs.next())
                    menus.add(mapRsToMenu(rs));
            }
            return menus;
        } catch (SQLException e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    private Menu mapRsToMenu(ResultSet rs) throws SQLException {
        return Menu.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .currentPrice(rs.getInt("current_price"))
                .build();
    }
}
