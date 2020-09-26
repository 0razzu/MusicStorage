package music_storage.request;


public class MusicianIdTrackIdRequest {
    private int musicianId;
    private int trackId;
    
    
    public MusicianIdTrackIdRequest(int musicianId, int trackId) {
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
