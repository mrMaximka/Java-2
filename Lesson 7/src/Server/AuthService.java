package Server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mainDB.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNickname(String login, String pass) {
        String query = String.format("select nickname from main\n" +
                "where login = '%s'\n" +
                "and password = '%s'", login, pass);
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String checkNickname(String login) {  // Проверяем наличие ника по логину, т.е. уникальность логина
        String query = String.format("select nickname from main\n" +
                "where login = '%s'", login);
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLogin(String nick) {    // Проверяем наличие логина по нику, т.е. уникальность ника
        String query = String.format("select login from main\n" +
                "where nickname = '%s'\n", nick);
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBlackList(String name) {    // Загружаем черный список
        String query = String.format("select blackList from main\n" +
                "where nickname = '%s'", name);
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveBlackList(String name, String list){ // Сохраняем черный список
        String query = String.format("update main\n" +
                "set blackList = '%s'\n" +
                "where nickname = '%s'", list, name);
        try {
           stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void regNewName(String login, String pass, String name){  // Регистрируем новый аккаунт
        String query = String.format("INSERT INTO main (\n" +
                "login, password, nickname) \n" +
        "VALUES ('%s', '%s', '%s')", login, pass, name);

        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}