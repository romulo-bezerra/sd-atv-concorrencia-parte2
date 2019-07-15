package br.edu.ifpb.sdatvconcorrenciaparte2.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConFactoryPostgreSQL {

    private final static String USER = "postgres";
    private final static String PASSWORD = "postgres";

    public static Connection getConnectionPostgres() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5433/managementuser", USER, PASSWORD);
    }

}
