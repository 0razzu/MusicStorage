package music_storage.dto;


public class IdDto {
    private int id;
    
    
    public IdDto(int id) {
        setId(id);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public int getId() {
        return id;
    }
}
