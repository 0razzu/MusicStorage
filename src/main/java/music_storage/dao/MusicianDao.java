package music_storage.dao;


import music_storage.error.ServerException;
import music_storage.model.Musician;
import music_storage.model.Track;

import java.util.List;


public interface MusicianDao {
    void addMusician(Musician musician) throws ServerException;
    
    Musician getMusician(int musicianId) throws ServerException;
    boolean musicianExists(int musicianId) throws ServerException;
    boolean allMusiciansExist(List<Integer> musicianIds) throws ServerException;
    
    void delMusician(int musicianId) throws ServerException;
    void delAllMusicians() throws ServerException;
    
    List<Track> getMusicianTracks(int musicianId) throws ServerException;
}
