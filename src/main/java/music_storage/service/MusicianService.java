package music_storage.service;


import com.google.gson.Gson;
import music_storage.dao.MusicianDao;
import music_storage.dao_impl.MusicianDaoImpl;
import music_storage.dto.*;
import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;
import music_storage.model.Musician;
import music_storage.model.Track;
import music_storage.util.JsonToDtoConverter;

import java.util.ArrayList;
import java.util.List;


public class MusicianService {
    private static final Gson gson = new Gson();
    private static final MusicianDao musicianDao = new MusicianDaoImpl();
    
    
    public String addMusician(String musicianJson) {
        try {
            MusicianDto musicianDto = JsonToDtoConverter.convert(musicianJson, MusicianDto.class);
            musicianDto.validate();
            Musician musician = new Musician(musicianDto.getName());
            
            musicianDao.addMusician(musician);
            
            return gson.toJson(new IdDto(musician.getId()));
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String getMusician(String idJson) {
        try {
            IdDto idDto = JsonToDtoConverter.convert(idJson, IdDto.class);
            Musician musician = musicianDao.getMusician(idDto.getId());
            
            return gson.toJson(
                    new MusicianDto(
                            musician.getId(),
                            musician.getName()
                    )
            );
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String delMusician(String idJson) {
        try {
            IdDto idDto = JsonToDtoConverter.convert(idJson, IdDto.class);
            musicianDao.delMusician(idDto.getId());
            
            return gson.toJson(new EmptyDto());
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String delAllMusicians(String emptyJson) {
        try {
            EmptyDto emptyDto = JsonToDtoConverter.convert(emptyJson, EmptyDto.class);
            musicianDao.delAllMusicians();
            
            return gson.toJson(new EmptyDto());
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
    
    
    public String getMusicianTracks(String idJson) {
        try {
            IdDto idDto = JsonToDtoConverter.convert(idJson, IdDto.class);
            
            if (!musicianDao.musicianExists(idDto.getId()))
                throw new ServerException(ErrorMessage.DB_MUSICIAN_NOT_FOUND);
            
            List<Track> tracks = musicianDao.getMusicianTracks(idDto.getId());
            List<TrackDto> tracksDto = new ArrayList<>(tracks.size());
            
            for (Track track: tracks)
                tracksDto.add(
                        new TrackDto(
                                track.getId(),
                                track.getName(),
                                track.getGenre()
                        )
                );
            
            return gson.toJson(tracksDto);
        } catch (ServerException e) {
            return gson.toJson(new ErrorDto(e.getMessage()));
        }
    }
}
