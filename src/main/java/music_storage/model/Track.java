package music_storage.model;


import java.util.Objects;


public class Track {
    private int id;
    private String name;
    private String genre;
    
    
    public Track(int id, String name, String genre) {
        setId(id);
        setName(name);
        setGenre(genre);
    }
    
    
    public Track(String name, String genre) {
        this(0, name, genre);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public String getGenre() {
        return genre;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return id == track.id &&
                name.equals(track.name) &&
                Objects.equals(genre, track.genre);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, genre);
    }
}
