package music_storage.dto;


import java.util.List;


public class TrackIdMusicianIdsDto {
    private int trackId;
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
}
