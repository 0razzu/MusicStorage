package music_storage.dao_impl;


import music_storage.dao.TrackDao;
import music_storage.database.DatabaseConnection;
import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;
import music_storage.model.Musician;
import music_storage.model.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TrackDaoImpl implements TrackDao {
    @Override
    public void addTrack(Track track) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String insertQuery = "INSERT track (name, genre) VALUES(?, ?)";
        
        try (PreparedStatement st = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, track.getName());
            st.setString(2, track.getGenre());
            
            if (st.executeUpdate() > 0) {
                ResultSet rs = st.getGeneratedKeys();
                
                if (rs.next())
                    track.setId(rs.getInt(1));
                
                rs.close();
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_ADD_TRACK);
        }
    }
    
    
    @Override
    public Track getTrack(int trackId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String getQuery = "SELECT name, genre FROM track WHERE id = ?";
        
        try (PreparedStatement st = con.prepareStatement(getQuery)) {
            st.setInt(1, trackId);
            
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next())
                    return new Track(
                            trackId,
                            rs.getString("name"),
                            rs.getString("genre")
                    );
                
                throw new ServerException(ErrorMessage.DB_TRACK_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_GET_TRACK);
        }
    }
    
    
    @Override
    public boolean trackExists(int trackId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String checkQuery = "SELECT 1 FROM track WHERE id = ?";
        
        try (PreparedStatement st = con.prepareStatement(checkQuery)) {
            st.setInt(1, trackId);
            
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_TRACK_EXISTS);
        }
    }
    
    
    @Override
    public void delTrack(int trackId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String deleteQuery = "DELETE FROM track WHERE id = ?";
        
        try (PreparedStatement st = con.prepareStatement(deleteQuery)) {
            st.setInt(1, trackId);
            
            st.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_DEL_TRACK);
        }
    }
    
    
    @Override
    public void delAllTracks() throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String deleteQuery = "DELETE FROM track";
        
        try (PreparedStatement st = con.prepareStatement(deleteQuery)) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_DEL_ALL_TRACKS);
        }
    }
    
    
    @Override
    public List<Musician> getTrackMusicians(int trackId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String getQuery = "SELECT musician_id AS id, musician.name " +
                "FROM musician_track JOIN musician ON musician_id = musician.id WHERE track_id = ?";
        
        try (PreparedStatement st = con.prepareStatement(getQuery)) {
            st.setInt(1, trackId);
            
            try (ResultSet rs = st.executeQuery()) {
                List<Musician> musicians = new ArrayList<>();
                
                while (rs.next())
                    musicians.add(new Musician(
                            rs.getInt("id"),
                            rs.getString("name"))
                    );
                
                return musicians;
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_GET_TRACK_MUSICIANS);
        }
    }
}
