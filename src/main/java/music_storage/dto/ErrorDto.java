package music_storage.dto;


public class ErrorDto {
    private String error;
    
    
    public ErrorDto(String error) {
        setError(error);
    }
    
    
    public void setError(String error) {
        this.error = error;
    }
    
    
    public String getError() {
        return error;
    }
}
