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
    
    
    public static String addMusician(String musicianJson) {
        return musicianService.addMusician(musicianJson);
    }
    
    
    public static String getMusician(String idJson) {
        return musicianService.getMusician(idJson);
    }
    
    
    public static String delMusician(String idJson) {
        return musicianService.delMusician(idJson);
    }
    
    
    public static String delAllMusicians(String emptyJson) {
        return musicianService.delAllMusicians(emptyJson);
    }
    
    
    public static String getMusicianTracks(String idJson) {
        return musicianService.getMusicianTracks(idJson);
    }
    
    
    public static String addTrack(String trackJson) {
        return trackService.addTrack(trackJson);
    }
    
    
    public static String getTrack(String idJson) {
        return trackService.getTrack(idJson);
    }
    
    
    public static String delTrack(String idJson) {
        return trackService.delTrack(idJson);
    }
    
    
    public static String delAllTracks(String emptyJson) {
        return trackService.delAllTracks(emptyJson);
    }
    
    
    public static String associateMusiciansWithTrack(String trackIdMusicianIdsJson) {
        return trackService.associateMusiciansWithTrack(trackIdMusicianIdsJson);
    }
    
    
    public static String getTrackMusicians(String idJson) {
        return trackService.getTrackMusicians(idJson);
    }
}
