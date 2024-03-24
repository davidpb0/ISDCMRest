/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author davidpb0
 */
public class Video {
    private int id;
    private String title;
    private String author;
    private String creationDate;
    private String duration;
    private int reproductions;
    private String description;
    private String format;
    private String path;

    // Constructor
    public Video(String title, String author, String duration, String description, String format, String path) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.title = title;
        this.author = author;
        this.creationDate = currentDateTime.format(formatter);
        this.duration = duration;
        this.reproductions = 0;
        this.description = description;
        this.format = format;
        this.path = path;
    }
    public Video(String title, String author, String duration, String description, String format, String path, int reproductions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.title = title;
        this.author = author;
        this.creationDate = currentDateTime.format(formatter);
        this.duration = duration;
        this.reproductions = reproductions;
        this.description = description;
        this.format = format;
        this.path = path;
    }
    
    public Video(String title, String author, String duration, String description, String format, String path, int reproductions, int id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.title = title;
        this.author = author;
        this.creationDate = currentDateTime.format(formatter);
        this.duration = duration;
        this.reproductions = reproductions;
        this.description = description;
        this.format = format;
        this.path = path;
        this.id = id;
    }


    // Getters & Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getReproductions() {
        return reproductions;
    }

    public void setReproductions(int reproductions) {
        this.reproductions = reproductions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }
    
    public String getFilePath() {
        return path;
    }
    
    public int getId() {
        return id;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
    public static List<byte[]> getAllVideosFromFolder(String folderPath) {
        List<byte[]> videoBinaries = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    byte[] videoBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                    videoBinaries.add(videoBytes);
                } catch (Exception e) {
                    System.out.println("Error reading video file: " + e.getMessage());
                }
            }
        }
        return videoBinaries;
    }
    
    public static Video getVideoByTitleAndAuthor(String title, String author) {
        Video video = null;
        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
            String query = "SELECT * FROM videos WHERE title = ? AND author = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, title);
                statement.setString(2, author);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        video = new Video(
                                resultSet.getString("title"),
                                resultSet.getString("author"),
                                resultSet.getString("duration"),
                                resultSet.getString("description"),
                                resultSet.getString("format"),
                                resultSet.getString("videoPath")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return video;
    }
    
     
    // Get all videos in DB
    public static List<Video> getAllVideos() {
        List<Video> videoList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
            String query = "SELECT * FROM videos";
            try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Video video = new Video(
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("duration"),
                            resultSet.getString("description" ),
                            resultSet.getString("format"),
                            resultSet.getString("videoPath"),
                            resultSet.getInt("reproductions")
                            
                    );
                    videoList.add(video);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return videoList;
    }
    
    //Save video to DB
    public boolean saveVideo() {
        if (!this.checkVideoExistance()){
            try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
                String query = "INSERT INTO videos (title, author, creationDate, duration, reproductions, description, format, videoPath) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, this.title);
                    statement.setString(2, this.author);
                    statement.setString(3, this.creationDate);
                    statement.setString(4, this.duration);
                    statement.setInt(5, this.reproductions);
                    statement.setString(6, this.description);
                    statement.setString(7, this.format);
                    statement.setString(8, this.path);
                    statement.executeUpdate();
                    return true;
                }
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        }
        return false;
    }
    
    public static Video getVideoByTitle(String title) {
        Video video = null;
        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
            String query = "SELECT * FROM videos WHERE title = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, title);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        video = new Video(
                                resultSet.getString("title"),
                                resultSet.getString("author"),
                                resultSet.getString("duration"),
                                resultSet.getString("description" ),
                                resultSet.getString("format"),
                                resultSet.getString("videoPath")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return video;
    }
    
    public static void updateReproductions(String title, String author) {
        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
            String query = "UPDATE videos SET reproductions = reproductions + 1 WHERE title = ? AND author = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, title);
                statement.setString(2, author);

                
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reproductions updated successfully for video with title: " + title);
                } else {
                    System.out.println("Failed to update reproductions for video with title: " + title);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
public boolean checkVideoExistance() {
    try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
        String query = "SELECT * FROM videos WHERE title = ? AND author = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(query)) {
            checkStatement.setString(1, this.title);
            checkStatement.setString(2, this.author);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                    return resultSet.next(); 
            }
            catch (SQLException e) {
                return true;
            }
        }
        
    } catch (SQLException e) {
        System.out.println(e);
    }
        return true;
}
    
public static Video getVideoByAuthor(String author) {
    Video video = null;
    try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2")) {
        String query = "SELECT * FROM videos WHERE author = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, author);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    video = new Video(
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("duration"),
                            resultSet.getString("description" ),
                            resultSet.getString("format"),
                            resultSet.getString("videoPath")
                    );
                }
            }
        }
    } catch (SQLException e) {
        System.out.println(e);
    }
    return video;
}   
    
    
    
}
