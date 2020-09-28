package music_storage.error;


public class ErrorMessage {
    public static final String DB_ADD_MUSICIAN = "Failed to add the musician";
    public static final String DB_ADD_TRACK = "Failed to add the track";
    public static final String DB_ASSOCIATE_MUSICIANS_WITH_TRACK = "Failed to associate the musicians with the track";
    public static final String DB_DEL_MUSICIAN = "Failed to delete the musician";
    public static final String DB_DEL_TRACK = "Failed to delete the track";
    public static final String DB_DEL_ALL_MUSICIANS = "Failed to delete all musicians";
    public static final String DB_DEL_ALL_TRACKS = "Failed to delete all tracks";
    public static final String DB_GET_MUSICIAN = "Failed to get the musician";
    public static final String DB_MUSICIAN_EXISTS = "Failed to check the musician’s existence";
    public static final String DB_MUSICIAN_NOT_FOUND = "Could not find a musician with this id";
    public static final String DB_GET_MUSICIAN_TRACKS = "Failed to get the musician’s tracks";
    public static final String DB_GET_TRACK = "Failed to get the track";
    public static final String DB_GET_TRACK_MUSICIANS = "Failed to get the track musicians";
    public static final String DB_TRACK_EXISTS = "Failed to check existence of the track";
    public static final String DB_TRACK_NOT_FOUND = "Could not find a track with this id";
    public static final String EMPTY_GENRE = "Genre cannot be empty";
    public static final String EMPTY_NAME = "Name cannot be null or empty";
    public static final String REQUEST_FORMAT_EXCEPTION = "The request contains some format errors";
}
