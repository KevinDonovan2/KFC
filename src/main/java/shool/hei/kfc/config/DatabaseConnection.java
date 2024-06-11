package shool.hei.kfc.config;

import hei.tantely.managementofrestaurantchain.exceptions.DatabaseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConnection {
    private final DatabaseSettings databaseSettings;

    public DatabaseConnection(DatabaseSettings databaseSettings) {
        this.databaseSettings = databaseSettings;
    }

    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    databaseSettings.getUrl(),
                    databaseSettings.getUsername(),
                    databaseSettings.getPassword()
            );
        } catch (SQLException e) {
            throw new DatabaseException("Error of the connection....");
        }
    }
}
