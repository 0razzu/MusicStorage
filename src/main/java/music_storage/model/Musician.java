package music_storage.model;


import java.util.Objects;


public class Musician {
    private int id;
    private String name;
    
    
    public Musician(int id, String name) {
        setId(id);
        setName(name);
    }
    
    
    public Musician(String name) {
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
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return id == musician.id &&
                name.equals(musician.name);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
