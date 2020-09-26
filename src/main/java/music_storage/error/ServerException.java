package music_storage.error;


public class ServerException extends Exception {
    private String error;
    
    
    public ServerException(String error) {
        this.error = error;
    }
    
    
    public String getError() {
        return error;
    }
}
