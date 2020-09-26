package music_storage.util;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import music_storage.error.ErrorMessage;
import music_storage.error.ServerException;


public class JsonToDtoConverter {
    private static final Gson gson = new Gson();
    
    
    public static <T> T convert(String json, Class<T> classOfT) throws ServerException {
        try {
            T dto = gson.fromJson(json, classOfT);
            
            if (dto == null)
                throw new ServerException(ErrorMessage.EMPTY_REQUEST);
            
            return dto;
        } catch (JsonSyntaxException e) {
            throw new ServerException(ErrorMessage.JSON_SYNTAX_EXCEPTION);
        }
    }
}
