package music_storage;


import music_storage.database.DatabaseConnection;
import music_storage.service.MusicianService;
import music_storage.service.TrackService;

import java.sql.SQLException;


public class Server {
    private static final MusicianService musicianService = new MusicianService();
    private static final TrackService trackService = new TrackService();
    
    
    public static void start() throws SQLException {
        DatabaseConnection.createConnection();
    }
    
    
    public static void stop() throws SQLException {
        DatabaseConnection.closeConnection();
    }
}
