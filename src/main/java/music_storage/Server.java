package music_storage;


import music_storage.dao.MusicianDao;
import music_storage.dao_impl.MusicianDaoImpl;
import music_storage.dao.TrackDao;
import music_storage.dao_impl.TrackDaoImpl;
import music_storage.database.DatabaseConnection;

import java.sql.SQLException;


public class Server {
    private static final MusicianDao musicianDao = new MusicianDaoImpl();
    private static final TrackDao trackDao = new TrackDaoImpl();
    
    
    public static void start() throws SQLException {
        DatabaseConnection.createConnection();
    }
    
    
    public static void stop() throws SQLException {
        DatabaseConnection.closeConnection();
    }
}
