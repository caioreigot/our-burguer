package com.ourburguer.backend.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory {

  public static Connection connect() {
    try {
      String url = "jdbc:sqlite:db/db.sqlite";
      return DriverManager.getConnection(url);
    } catch (SQLException err) {
      System.err.println("Não foi possível conectar ao banco. " + err.getMessage());
      return null;
    }
  }

  public static void closeConnection(Connection connection) {
    try {
      if (!connection.isClosed()) {
        connection.close();
      }
    } catch (SQLException err) {
      System.err.println("Erro ao fechar conexão. " + err.getMessage());
    }
  }

  public static void closeConnection(Connection connection, PreparedStatement stmt) {
    try {
      closeConnection(connection);
      if (stmt != null) {
        stmt.close();
      }
    } catch (SQLException err) {
      System.err.println("Erro ao fechar conexão. " + err.getMessage());
    }
  }

  public static void closeConnection(Connection connection, PreparedStatement stmt, ResultSet rs) {
    try {
      closeConnection(connection, stmt);
      if (rs != null) {
        rs.close();
      }
    } catch (SQLException err) {
      System.err.println("Erro ao fechar conexão. " + err.getMessage());
    }
  }
}
