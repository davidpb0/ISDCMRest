package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    public Video(String title, String author, String duration, String description, String format, String path, int reproductions, int id, String creationDate) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.reproductions = reproductions;
        this.description = description;
        this.format = format;
        this.path = path;
        this.id = id;
        this.creationDate = creationDate;
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
    
    public String getPath() {
        return path;
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
    
    public static List<Video> getVideosByFilter(String filter, String value) {
        List<Video> videoList = new ArrayList<>();
        
        // List of valid filters
        Set<String> validFilters = new HashSet<>(Arrays.asList("title", "author", "creationdate"));

        if (!validFilters.contains(filter)) {
            System.out.println("Invalid filter " + filter);
            return videoList;
        }

        String query = "SELECT * FROM videos WHERE ";
        boolean isDateFilter = false;

        if (filter.equals("creationdate")) {
            // Handle different combinations of date
            isDateFilter = true;
            if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                query += "creationdate = ?";
            } else if (value.matches("\\d{4}-\\d{2}")) {
                query += "YEAR(creationdate) = ? AND MONTH(creationdate) = ?";
            } else if (value.matches("\\d{4}")) {
                query += "YEAR(creationdate) = ?";
            } else {
                System.out.println("Invalid date format");
                return videoList;
            }
        } else {
            query += filter + " = ?";
        }
        try (Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/pr2", "pr2", "pr2");
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            if (isDateFilter) {
                if (value.matches("\\d{4}-\\d{2}-\\d{2}") || value.matches("\\d{4}-\\d{2}")) {
                    statement.setString(1, value);
                    if (value.matches("\\d{4}-\\d{2}")) {
                        statement.setString(1, value.substring(0, 4)); // For year
                        statement.setString(2, value.substring(5, 7)); // For month
                    }
                } else if (value.matches("\\d{4}")) {
                    statement.setString(1, value);
                }
            } else {
                statement.setString(1, value);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Video video = new Video(
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("duration"),
                            resultSet.getString("description"),
                            resultSet.getString("format"),
                            resultSet.getString("videoPath"),
                            resultSet.getInt("reproductions"),
                            resultSet.getInt("id"),
                            resultSet.getString("creationdate")
                    );
                    videoList.add(video);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return videoList;
    }
    
}
