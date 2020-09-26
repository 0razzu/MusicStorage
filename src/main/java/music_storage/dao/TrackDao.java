package music_storage.dao;


import music_storage.error.ServerException;
import music_storage.model.Musician;
import music_storage.model.Track;

import java.util.List;


public interface TrackDao {
    void addTrack(Track track) throws ServerException;
    
    Track getTrack(int trackId) throws ServerException;
    boolean trackExists(int trackId) throws ServerException;
    
    void delTrack(int trackId) throws ServerException;
    void delAllTracks() throws ServerException;
    
    void associateMusiciansWithTrack(int trackId, int... musicianIds) throws ServerException;
    List<Musician> getTrackMusicians(int trackId) throws ServerException;
}
