package music_storage.dto;


import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;

import java.util.List;


public class TrackIdMusicianIdsDto {
    private Integer trackId;
    private List<Integer> musicianIds;
    
    
    public TrackIdMusicianIdsDto(int trackId, List<Integer> musicianIds) {
        this.trackId = trackId;
        this.musicianIds = musicianIds;
    }
    
    
    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
    
    
    public void setMusicianIds(List<Integer> musicianIds) {
        this.musicianIds = musicianIds;
    }
    
    
    public int getTrackId() {
        return trackId;
    }
    
    
    public List<Integer> getMusicianIds() {
        return musicianIds;
    }
    
    
    public void validate() throws ServerException {
        if (trackId == null || musicianIds.isEmpty() || musicianIds.contains(null))
            throw new ServerException(ErrorMessage.REQUEST_FORMAT_EXCEPTION);
    }
}
