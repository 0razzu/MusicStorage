package music_storage.dto;


public class TrackIdMusicianIdsDto {
    private int trackId;
    private int[] musicianIds;
    
    
    public TrackIdMusicianIdsDto(int trackId, int[] musicianIds) {
        this.trackId = trackId;
        this.musicianIds = musicianIds;
    }
    
    
    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
    
    
    public void setMusicianIds(int[] musicianIds) {
        this.musicianIds = musicianIds;
    }
    
    
    public int getTrackId() {
        return trackId;
    }
    
    
    public int[] getMusicianIds() {
        return musicianIds;
    }
}
