/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author davidpb0
 */
public class User {
    private String nickname;
    private String username;
    private String surnames;
    private String email;
    private String password;
    
    // Constructor
    public User(String nickname, String username, String surnames, String email, String password) {
        this.nickname = nickname;
        this.username = username;
        this.surnames = surnames;
        this.email = email;
        this.password = password;
    }
    
    // Getters & Setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public static boolean authenticateUser(String nickname, String password) {        
           String query = "SELECT * FROM users WHERE nickname = ? AND password = ?";

           try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2");
                PreparedStatement statement = connection.prepareStatement(query)) {
               statement.setString(1, nickname);
               statement.setString(2, password);

               try (ResultSet resultSet = statement.executeQuery()) {
                   return resultSet.next();
               }
           } catch (SQLException e) {
               return false; 
           }
    }
    
}
