package shool.hei.kfc.repositories.impl;

import hei.tantely.managementofrestaurantchain.entities.Ingredient;
import hei.tantely.managementofrestaurantchain.entities.IngredientMenu;
import hei.tantely.managementofrestaurantchain.entities.Menu;
import hei.tantely.managementofrestaurantchain.exceptions.InternalServerException;
import hei.tantely.managementofrestaurantchain.exceptions.NotFoundException;
import hei.tantely.managementofrestaurantchain.repositories.IngredientMenuRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientMenuRepositoryImpl implements IngredientMenuRepository {
    private final Connection connection;


    public IngredientMenuRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public IngredientMenu save(IngredientMenu toSave) {
        final var insertSql = "INSERT INTO ingredient_menu (quantity, menu_id, ingredient_id) VALUES ( ?, ?, ?)";
        final var updateSql = "UPDATE ingredient_menu SET quantity = ?, menu_id  = ? , ingredient_id  = ? WHERE id = ?";
        final var query = toSave.getId() == null ? insertSql : updateSql;
        try (final var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, toSave.getQuantity());

            if (toSave.getMenu().getId() == null) {
                stmt.setNull(2, Types.BIGINT);
            } else {
                stmt.setInt(2, toSave.getMenu().getId());
            }

            if (toSave.getIngredient().getId() == null) {
                stmt.setNull(3, Types.BIGINT);
            } else {
                stmt.setInt(3, toSave.getIngredient().getId());
            }

            if (toSave.getId() != null) {
                stmt.setInt(4, toSave.getId());
            }

            var rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new InternalServerException("mistake when creating or updating a ingredient menu...");
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
    public IngredientMenu findByMenuId(Integer id) {
        final var query = "SELECT * FROM ingredient_menu WHERE menu_id = ?";
        return getIngredientMenu(id, query);
    }

    @Override
    public IngredientMenu findByIngredientId(Integer id) {
        final var query = "SELECT * FROM ingredient_menu WHERE ingredient_id = ?";
        return getIngredientMenu(id, query);
    }

    @Override
    public IngredientMenu findByIngredientIdAndMenuId(Integer ingredientId, Integer menuId) {
        var query = "SELECT * FROM ingredient_menu WHERE ingredient_id = ? AND menu_id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ingredientId);
            stmt.setInt(2, menuId);
             final var rs = stmt.executeQuery();
                if (rs.next())
                    return mapRsToIngredientMenu(rs);

            throw new NotFoundException("Ingredient menu not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void deleteByIngredientIdAndMenuId(IngredientMenu ingredientMenu) {
        final var query = "DELETE FROM ingredient_menu WHERE ingredient_id = ? AND menu_id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ingredientMenu.getIngredient().getId());
            stmt.setInt(2, ingredientMenu.getMenu().getId());
            var rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new InternalServerException("Delete ingredient menu not valid");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<IngredientMenu> findAllByMenuId(Integer menuId) {
        var ingredientMenus = new ArrayList<IngredientMenu>();
        var query = "SELECT * FROM ingredient_menu WHERE menu_id = ?";
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, menuId);
            final var rs = stmt.executeQuery();
            while (rs.next())
                ingredientMenus.add(mapRsToIngredientMenu(rs));

            return ingredientMenus;
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    private IngredientMenu getIngredientMenu(Integer id, String query) {
        try (final var stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (final var rs = stmt.executeQuery()) {
                if (rs.next())
                    return mapRsToIngredientMenu(rs);
            }
            throw new NotFoundException("Ingredient menu not found !");
        } catch (SQLException e) {
            throw new NotFoundException(e.getMessage());
        }
    }


    private IngredientMenu mapRsToIngredientMenu(ResultSet rs) throws SQLException {
        return (new IngredientMenu())
                .setId(rs.getInt("id"))
                .setIngredient(Ingredient.builder().id(rs.getInt("ingredient_id")).build())
                .setMenu(Menu.builder().id(rs.getInt("menu_id")).build())
                .setQuantity(rs.getDouble("quantity"));
    }
}
