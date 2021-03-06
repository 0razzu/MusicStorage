package music_storage.service;


import com.google.gson.Gson;
import music_storage.dao.MusicianDao;
import music_storage.dao.TrackDao;
import music_storage.dao_impl.MusicianDaoImpl;
import music_storage.dao_impl.TrackDaoImpl;
import music_storage.dto.*;
import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;
import music_storage.model.Musician;
import music_storage.model.Track;
import music_storage.util.JsonToDtoConverter;

import java.util.ArrayList;
import java.util.List;


public class TrackService {
    private static final Gson gson = new Gson();
    private static final TrackDao trackDao = new TrackDaoImpl();
    private static final MusicianDao musicianDao = new MusicianDaoImpl();
    
    
    public String addTrack(String trackJson) {
        try {
            TrackDto trackDto = JsonToDtoConverter.convert(trackJson, TrackDto.class);
            trackDto.validate();
            Track track = new Track(
                    trackDto.getName(),
                    trackDto.getGenre()
            );
            
            trackDao.addTrack(track);
            
            return gson.toJson(new IdDto(track.getId()));
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String getTrack(String idJson) {
        try {
            IdDto idDto = JsonToDtoConverter.convert(idJson, IdDto.class);
            idDto.validate();
            Track track = trackDao.getTrack(idDto.getId());
            
            return gson.toJson(
                    new TrackDto(
                            track.getId(),
                            track.getName(),
                            track.getGenre()
                    )
            );
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String delTrack(String idJson) {
        try {
            IdDto idDto = JsonToDtoConverter.convert(idJson, IdDto.class);
            idDto.validate();
    
            if (!trackDao.trackExists(idDto.getId()))
                throw new ServerException(ErrorMessage.DB_TRACK_NOT_FOUND);
    
            trackDao.delTrack(idDto.getId());
            
            return gson.toJson(new EmptyDto());
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String delAllTracks(String emptyJson) {
        try {
            EmptyDto emptyDto = JsonToDtoConverter.convert(emptyJson, EmptyDto.class);
            trackDao.delAllTracks();
            
            return gson.toJson(new EmptyDto());
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String associateMusiciansWithTrack(String trackIdMusicianIdsJson) {
        try {
            TrackIdMusicianIdsDto trackIdMusicianIdsDto =
                    JsonToDtoConverter.convert(trackIdMusicianIdsJson, TrackIdMusicianIdsDto.class);
            trackIdMusicianIdsDto.validate();
            
            if (!trackDao.trackExists(trackIdMusicianIdsDto.getTrackId()))
                throw new ServerException(ErrorMessage.DB_TRACK_NOT_FOUND);
            
            if (!musicianDao.allMusiciansExist(trackIdMusicianIdsDto.getMusicianIds()))
                throw new ServerException(ErrorMessage.DB_MUSICIAN_NOT_FOUND);
            
            trackDao.associateMusiciansWithTrack(trackIdMusicianIdsDto.getTrackId(), trackIdMusicianIdsDto.getMusicianIds());
            
            return gson.toJson(new EmptyDto());
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String getTrackMusicians(String idJson) {
        try {
            IdDto idDto = JsonToDtoConverter.convert(idJson, IdDto.class);
            idDto.validate();
            
            if (!trackDao.trackExists(idDto.getId()))
                throw new ServerException(ErrorMessage.DB_TRACK_NOT_FOUND);
            
            List<Musician> musicians = trackDao.getTrackMusicians(idDto.getId());
            List<MusicianDto> musiciansDto = new ArrayList<>(musicians.size());
            
            for (Musician musician: musicians)
                musiciansDto.add(
                        new MusicianDto(
                                musician.getId(),
                                musician.getName()
                        )
                );
            
            return gson.toJson(musiciansDto);
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
}
