package music_storage;


import com.google.gson.Gson;
import music_storage.dto.EmptyDto;
import music_storage.dto.ErrorDto;
import music_storage.dto.IdDto;
import music_storage.error.ErrorMessage;
import music_storage.model.Musician;
import music_storage.model.Track;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class TestServer {
    static final Gson gson = new Gson();
    
    static final Musician mAndyRocks = new Musician("Andy Rocks");
    static final Musician mMiaAmo = new Musician("Mia Amo");
    static final Musician mEmmaBreak = new Musician("Emma Break");
    static final Musician mChrisEagle = new Musician("Chris Eagle");
    static final Musician mDannyWhite = new Musician("Danny White");
    
    static final Track tAlone = new Track("Alone", "Pop");
    static final Track tJustice = new Track("Justice", "Punk");
    static final Track tSearching = new Track("Searching", "Emo");
    static final Track tHidingInTheWoods = new Track("Hiding in the Woods", "Pop");
    static final Track tThePoison = new Track("The Poison", "Deathcore");
    static final Track tSketch = new Track("sketch", null);
    
    static final String emptyJson = gson.toJson(new EmptyDto());
    
    
    @BeforeAll
    static void startServer() throws SQLException {
        Server.start();
    }
    
    
    @AfterAll
    static void stopServer() throws SQLException {
        Server.stop();
    }
    
    
    @AfterEach
    void clearDatabase() {
        Server.delAllTracks("{}");
        Server.delAllMusicians("{}");
    }
    
    
    @Test
    void testAddMusician() {
        int mAndyRocksId = gson.fromJson(Server.addMusician(gson.toJson(mAndyRocks)), IdDto.class).getId();
        int mMiaAmoId = gson.fromJson(Server.addMusician(gson.toJson(mMiaAmo)), IdDto.class).getId();
        int mEmmaBreakId = gson.fromJson(Server.addMusician(gson.toJson(mEmmaBreak)), IdDto.class).getId();
        
        assertAll(
                () -> assertNotEquals(mAndyRocksId, mMiaAmoId),
                () -> assertNotEquals(mAndyRocksId, mEmmaBreakId),
                () -> assertNotEquals(mMiaAmoId, mEmmaBreakId)
        );
    }
    
    
    @Test
    void testAddMusicianBadRequests() {
        String emptyName = gson.fromJson(Server.addMusician(gson.toJson(new Musician("  "))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.addMusician("f"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.addMusician(""), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.addMusician("{'wrongField':1}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.EMPTY_NAME, wrongField),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.EMPTY_NAME, wrongField)
        );
    }
    
    
    @Test
    void testGetMusician() {
        String mAndyRocksIdJson = Server.addMusician(gson.toJson(mAndyRocks));
        String mChrisEagleIdJson = Server.addMusician(gson.toJson(mChrisEagle));
        
        mAndyRocks.setId(gson.fromJson(mAndyRocksIdJson, IdDto.class).getId());
        mChrisEagle.setId(gson.fromJson(mChrisEagleIdJson, IdDto.class).getId());
        
        assertAll(
                () -> assertEquals(mAndyRocks, gson.fromJson(Server.getMusician(mAndyRocksIdJson), Musician.class)),
                () -> assertEquals(mChrisEagle, gson.fromJson(Server.getMusician(mChrisEagleIdJson), Musician.class))
        );
    }
    
    
    @Test
    void testGetMusicianBadRequests() {
        String notFound = gson.fromJson(Server.getMusician(gson.toJson(new IdDto(1))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.getMusician("+p"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.getMusician(" "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.getMusician("{'message':'hello'}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_MUSICIAN_NOT_FOUND, notFound),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
    
    
    @Test
    void testDelMusician() {
        String mAndyRocksIdJson = Server.addMusician(gson.toJson(mAndyRocks));
        String mChrisEagleIdJson = Server.addMusician(gson.toJson(mChrisEagle));
    
        mChrisEagle.setId(gson.fromJson(mChrisEagleIdJson, IdDto.class).getId());
        
        assertAll(
                () -> assertEquals(emptyJson, Server.delMusician(mAndyRocksIdJson)),
                () -> assertEquals(ErrorMessage.DB_MUSICIAN_NOT_FOUND,
                        gson.fromJson(Server.getMusician(mAndyRocksIdJson), ErrorDto.class).getError()),
                () -> assertEquals(mChrisEagle, gson.fromJson(Server.getMusician(mChrisEagleIdJson), Musician.class))
        );
    }
    
    
    @Test
    void testDelMusicianBadRequests() {
        String notFound = gson.fromJson(Server.delMusician(gson.toJson(new IdDto(1))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.delMusician("-"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.delMusician("  "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.getMusician("{'value':0}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_MUSICIAN_NOT_FOUND, notFound),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
    
    
    @Test
    void testDelAllMusiciansBadRequests() {
        String syntax = gson.fromJson(Server.delAllMusicians("-"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.delAllMusicians("  "), ErrorDto.class).getError();
        String wrongField = Server.delAllMusicians("{'value':0}");
        
        assertAll(
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(emptyJson, wrongField)
        );
    }
}
