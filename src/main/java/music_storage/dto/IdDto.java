package music_storage.dto;


import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;


public class IdDto {
    private Integer id;
    
    
    public IdDto(int id) {
        setId(id);
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    
    public int getId() {
        return id;
    }
    
    
    public void validate() throws ServerException {
        if (id == null)
            throw new ServerException(ErrorMessage.REQUEST_FORMAT_EXCEPTION);
    }
}
