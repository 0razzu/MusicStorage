package music_storage.dao_impl;


import music_storage.dao.MusicianDao;
import music_storage.database.DatabaseConnection;
import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;
import music_storage.model.Musician;
import music_storage.model.Track;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MusicianDaoImpl implements MusicianDao {
    @Override
    public void addMusician(Musician musician) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String insertQuery = "INSERT musician (name) VALUES(?)";
        
        try (PreparedStatement st = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, musician.getName());
            
            if (st.executeUpdate() > 0) {
                ResultSet rs = st.getGeneratedKeys();
                
                if (rs.next())
                    musician.setId(rs.getInt(1));
                
                rs.close();
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_ADD_MUSICIAN);
        }
    }
    
    
    @Override
    public Musician getMusician(int musicianId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String getQuery = "SELECT name FROM musician WHERE id = ?";
        
        try (PreparedStatement st = con.prepareStatement(getQuery)) {
            st.setInt(1, musicianId);
            
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next())
                    return new Musician(
                            musicianId,
                            rs.getString("name")
                    );
                
                throw new ServerException(ErrorMessage.DB_MUSICIAN_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_GET_MUSICIAN);
        }
    }
    
    
    @Override
    public boolean musicianExists(int musicianId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String checkQuery = "SELECT 1 FROM musician WHERE id = ?";
        
        try (PreparedStatement st = con.prepareStatement(checkQuery)) {
            st.setInt(1, musicianId);
            
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_MUSICIAN_EXISTS);
        }
    }
    
    
    @Override
    public void delMusician(int musicianId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String deleteQuery = "DELETE FROM musician WHERE id = ?";
        
        try (PreparedStatement st = con.prepareStatement(deleteQuery)) {
            st.setInt(1, musicianId);
            
            st.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_DEL_MUSICIAN);
        }
    }
    
    
    @Override
    public void delAllMusicians() throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String deleteQuery = "DELETE FROM musician";
        
        try (PreparedStatement st = con.prepareStatement(deleteQuery)) {
            st.executeUpdate();
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_DEL_ALL_MUSICIANS);
        }
    }
    
    
    @Override
    public List<Track> getMusicianTracks(int musicianId) throws ServerException {
        Connection con = DatabaseConnection.getConnection();
        String getQuery = "SELECT track_id AS id, track.name, track.genre " +
                "FROM musician_track JOIN track ON track_id = track.id WHERE musician_id = ?";
        
        try (PreparedStatement st = con.prepareStatement(getQuery)) {
            st.setInt(1, musicianId);
            
            try (ResultSet rs = st.executeQuery()) {
                List<Track> tracks = new ArrayList<>();
                
                while (rs.next())
                    tracks.add(new Track(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("genre")
                            )
                    );
                
                return tracks;
            }
        } catch (SQLException e) {
            throw new ServerException(ErrorMessage.DB_GET_MUSICIAN_TRACKS);
        }
    }
}
