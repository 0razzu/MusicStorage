package music_storage.dto;


import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;

import java.util.Objects;


public class MusicianIdTrackIdDto {
    private Integer musicianId;
    private Integer trackId;
    
    
    public MusicianIdTrackIdDto(int musicianId, int trackId) {
        setMusicianId(musicianId);
        setTrackId(trackId);
    }
    
    
    public void setMusicianId(int musicianId) {
        this.musicianId = musicianId;
    }
    
    
    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
    
    
    public int getMusicianId() {
        return musicianId;
    }
    
    
    public int getTrackId() {
        return trackId;
    }
    
    
    public void validate() throws ServerException {
        if (musicianId == null || trackId == null)
            throw new ServerException(ErrorMessage.REQUEST_FORMAT_EXCEPTION);
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicianIdTrackIdDto that = (MusicianIdTrackIdDto) o;
        return Objects.equals(musicianId, that.musicianId) &&
                Objects.equals(trackId, that.trackId);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(musicianId, trackId);
    }
}
