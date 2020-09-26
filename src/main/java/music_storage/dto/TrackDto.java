package music_storage.dto;


import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;

import java.util.Objects;


public class TrackDto {
    private int id;
    private String name;
    private String genre;
    
    
    public TrackDto(int id, String name, String genre) {
        setId(id);
        setName(name);
        setGenre(genre);
    }
    
    
    public TrackDto(String name, String genre) {
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
    
    
    public void validate() throws ServerException {
        if (name == null || name.isBlank())
            throw new ServerException(ErrorMessage.EMPTY_NAME);
        
        if (genre != null && genre.isBlank())
            throw new ServerException(ErrorMessage.EMPTY_GENRE);
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackDto trackDto = (TrackDto) o;
        return id == trackDto.id &&
                Objects.equals(name, trackDto.name) &&
                Objects.equals(genre, trackDto.genre);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, genre);
    }
}
