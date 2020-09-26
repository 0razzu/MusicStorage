package music_storage.request;


public class IdRequest {
    private int id;
    
    
    public IdRequest(int id) {
        setId(id);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public int getId() {
        return id;
    }
}
