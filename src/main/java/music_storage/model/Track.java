package music_storage.model;


import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;

import java.util.Objects;


public class Track {
    private int id;
    private String name;
    private String genre;
    
    
    public Track(int id, String name, String genre) throws ServerException {
        setId(id);
        setName(name);
        setGenre(genre);
    }
    
    
    public Track(String name, String genre) throws ServerException {
        this(0, name, genre);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setName(String name) throws ServerException {
        if (name == null || name.isBlank())
            throw new ServerException(ErrorMessage.EMPTY_NAME);
        
        this.name = name;
    }
    
    
    public void setGenre(String genre) throws ServerException {
        if (genre != null && genre.isBlank())
            throw new ServerException(ErrorMessage.EMPTY_GENRE);
        
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
