package com.ipn.HolaSpring.Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MySQLController {

    // Parámetros de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/tarea2?useSSL=false&serverTimezone=UTC";
    private static final String USER = "admin"; // Usar el nuevo usuario 'admin'
    private static final String PASSWORD = "admin"; // Contraseña 'admin'

    @GetMapping("/testdb")
    public String testDatabaseConnection() {
        Connection connection = null;
        StringBuilder result = new StringBuilder();

        try {
            // Conectar a la base de datos
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            result.append("Conexión exitosa a la base de datos MySQL\n");

        } catch (SQLException e) {
            result.append("Error al conectar a la base de datos: " + e.getMessage() + "\n");
            return result.toString();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    result.append("Conexión cerrada");
                }
            } catch (SQLException e) {
                result.append("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        
        return result.toString();
    }
}