package music_storage.dto;


import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;

import java.util.Objects;


public class MusicianDto {
    private int id;
    private String name;
    
    
    public MusicianDto(int id, String name) {
        setId(id);
        setName(name);
    }
    
    
    public MusicianDto(String name) {
        this(0, name);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public String getName() {
        return name;
    }
    
    
    public void validate() throws ServerException {
        if (name == null || name.isBlank())
            throw new ServerException(ErrorMessage.EMPTY_NAME);
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicianDto that = (MusicianDto) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
