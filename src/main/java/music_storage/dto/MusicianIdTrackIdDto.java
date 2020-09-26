package music_storage.dto;


public class MusicianIdTrackIdDto {
    private int musicianId;
    private int trackId;
    
    
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
}
