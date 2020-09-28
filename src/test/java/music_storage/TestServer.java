package music_storage;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import music_storage.dto.EmptyDto;
import music_storage.dto.ErrorDto;
import music_storage.dto.IdDto;
import music_storage.dto.TrackIdMusicianIdsDto;
import music_storage.error.ErrorMessage;
import music_storage.model.Musician;
import music_storage.model.Track;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class TestServer {
    static final Gson gson = new Gson();
    static final Type musicianSetType = new TypeToken<Set<Musician>>(){}.getType();
    static final Type trackSetType = new TypeToken<Set<Track>>(){}.getType();
    
    static final Musician mAndyRocks = new Musician("Andy Rocks");
    static final Musician mMiaAmo = new Musician("Mia Amo");
    static final Musician mEmmaBreak = new Musician("Emma Break");
    static final Musician mChrisEagle = new Musician("Chris Eagle");
    
    static final Track tAlone = new Track("Alone", "Pop");
    static final Track tJustice = new Track("Justice", "Punk");
    static final Track tSearching = new Track("Searching", "Pop");
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
        Server.delAllTracks(emptyJson);
        Server.delAllMusicians(emptyJson);
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
                () -> assertEquals(ErrorMessage.EMPTY_NAME, emptyName),
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
    
    
    @Test
    void testAddTrack() {
        int tAloneId = gson.fromJson(Server.addTrack(gson.toJson(tAlone)), IdDto.class).getId();
        int tJusticeId = gson.fromJson(Server.addMusician(gson.toJson(tAlone)), IdDto.class).getId();
        int tSketchId = gson.fromJson(Server.addMusician(gson.toJson(tAlone)), IdDto.class).getId();
        
        assertAll(
                () -> assertNotEquals(tAloneId, tJusticeId),
                () -> assertNotEquals(tAloneId, tSketchId),
                () -> assertNotEquals(tJusticeId, tSketchId)
        );
    }
    
    
    @Test
    void testAddTrackBadRequests() {
        String emptyName = gson.fromJson(Server.addTrack(gson.toJson(new Track("", "Pop"))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.addTrack("track"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.addTrack(""), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.addTrack("{'what':'nothing'}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.EMPTY_NAME, emptyName),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.EMPTY_NAME, wrongField)
        );
    }
    
    
    @Test
    void testGetTrack() {
        String tAloneIdJson = Server.addTrack(gson.toJson(tAlone));
        String tSketchIdJson = Server.addTrack(gson.toJson(tSketch));
        
        tAlone.setId(gson.fromJson(tAloneIdJson, IdDto.class).getId());
        tSketch.setId(gson.fromJson(tSketchIdJson, IdDto.class).getId());
        
        assertAll(
                () -> assertEquals(tAlone, gson.fromJson(Server.getTrack(tAloneIdJson), Track.class)),
                () -> assertEquals(tSketch, gson.fromJson(Server.getTrack(tSketchIdJson), Track.class))
        );
    }
    
    
    @Test
    void testGetTrackBadRequests() {
        String notFound = gson.fromJson(Server.getTrack(gson.toJson(new IdDto(1))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.getTrack("+p"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.getTrack(" "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.getTrack("{'num':9}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_TRACK_NOT_FOUND, notFound),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
    
    
    @Test
    void testDelTrack() {
        String tAloneIdJson = Server.addTrack(gson.toJson(tAlone));
        String tSearchingIdJson = Server.addTrack(gson.toJson(tSearching));
        
        tSearching.setId(gson.fromJson(tSearchingIdJson, IdDto.class).getId());
        
        assertAll(
                () -> assertEquals(emptyJson, Server.delTrack(tAloneIdJson)),
                () -> assertEquals(ErrorMessage.DB_TRACK_NOT_FOUND,
                        gson.fromJson(Server.getTrack(tAloneIdJson), ErrorDto.class).getError()),
                () -> assertEquals(tSearching, gson.fromJson(Server.getTrack(tSearchingIdJson), Track.class))
        );
    }
    
    
    @Test
    void testDelTrackBadRequests() {
        String notFound = gson.fromJson(Server.delTrack(gson.toJson(new IdDto(1))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.delTrack("-"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.delTrack("  "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.delTrack("{'value':0}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_TRACK_NOT_FOUND, notFound),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
    
    
    @Test
    void testDelAllTracksBadRequests() {
        String syntax = gson.fromJson(Server.delAllTracks("/"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.delAllTracks("  "), ErrorDto.class).getError();
        String wrongField = Server.delAllTracks("{'value':0}");
        
        assertAll(
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(emptyJson, wrongField)
        );
    }
    
    
    @Test
    void testAssociateMusiciansWithTrackOneToOne() {
        String mAndyRocksIdJson = Server.addMusician(gson.toJson(mAndyRocks));
        String mMiaAmoIdJson = Server.addMusician(gson.toJson(mMiaAmo));
        String tAloneIdJson = Server.addTrack(gson.toJson(tAlone));
        String tSearchingIdJson = Server.addTrack(gson.toJson(tSearching));
        
        int mAndyRocksId = gson.fromJson(mAndyRocksIdJson, IdDto.class).getId();
        int mMiaAmoId = gson.fromJson(mMiaAmoIdJson, IdDto.class).getId();
        int tAloneId = gson.fromJson(tAloneIdJson, IdDto.class).getId();
        int tSearchingId = gson.fromJson(tSearchingIdJson, IdDto.class).getId();
        
        mAndyRocks.setId(mAndyRocksId);
        mMiaAmo.setId(mMiaAmoId);
        tAlone.setId(tAloneId);
        tSearching.setId(tSearchingId);
        
        assertAll(
                () -> assertEquals(emptyJson, Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tAloneId, List.of(mAndyRocksId))))),
                () -> assertEquals(emptyJson, Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tSearchingId, List.of(mMiaAmoId))))),
                () -> assertEquals(Set.of(tAlone),
                        gson.fromJson(Server.getMusicianTracks(mAndyRocksIdJson), trackSetType)),
                () -> assertEquals(Set.of(tSearching),
                        gson.fromJson(Server.getMusicianTracks(mMiaAmoIdJson), trackSetType)),
                () -> assertEquals(Set.of(mAndyRocks),
                        gson.fromJson(Server.getTrackMusicians(tAloneIdJson), musicianSetType)),
                () -> assertEquals(Set.of(mMiaAmo),
                        gson.fromJson(Server.getTrackMusicians(tSearchingIdJson), musicianSetType))
        );
    }
    
    
    @Test
    void testAssociateMusiciansWithTrackManyToMany() {
        String mAndyRocksIdJson = Server.addMusician(gson.toJson(mAndyRocks));
        String mMiaAmoIdJson = Server.addMusician(gson.toJson(mMiaAmo));
        String mEmmaBreakIdJson = Server.addMusician(gson.toJson(mEmmaBreak));
        String mChrisEagleIdJson = Server.addMusician(gson.toJson(mChrisEagle));
        
        int mAndyRocksId = gson.fromJson(mAndyRocksIdJson, IdDto.class).getId();
        int mMiaAmoId = gson.fromJson(mMiaAmoIdJson, IdDto.class).getId();
        int mEmmaBreakId = gson.fromJson(mEmmaBreakIdJson, IdDto.class).getId();
        int mChrisEagleId = gson.fromJson(mAndyRocksIdJson, IdDto.class).getId();
        
        mAndyRocks.setId(mAndyRocksId);
        mMiaAmo.setId(mMiaAmoId);
        mEmmaBreak.setId(mEmmaBreakId);
        mChrisEagle.setId(mChrisEagleId);
        
        String tAloneIdJson = Server.addTrack(gson.toJson(tAlone));
        String tJusticeIdJson = Server.addTrack(gson.toJson(tJustice));
        String tThePoisonIdJson = Server.addTrack(gson.toJson(tThePoison));
        String tSketchIdJson = Server.addTrack(gson.toJson(tSketch));
        
        int tAloneId = gson.fromJson(tAloneIdJson, IdDto.class).getId();
        int tJusticeId = gson.fromJson(tJusticeIdJson, IdDto.class).getId();
        int tThePoisonId = gson.fromJson(tThePoisonIdJson, IdDto.class).getId();
        int tSketchId = gson.fromJson(tSketchIdJson, IdDto.class).getId();
        
        tAlone.setId(tAloneId);
        tJustice.setId(tJusticeId);
        tThePoison.setId(tThePoisonId);
        tSketch.setId(tSketchId);
        
        assertAll(
                () -> assertEquals(emptyJson, Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tAloneId, List.of(mAndyRocksId))))),
                () -> assertEquals(emptyJson, Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tJusticeId, List.of(mAndyRocksId, mMiaAmoId, mEmmaBreakId))))),
                () -> assertEquals(emptyJson, Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tSketchId, List.of(mAndyRocksId, mEmmaBreakId)))))
        );
        
        assertAll(
                () -> assertEquals(Set.of(tAlone, tJustice, tSketch),
                        gson.fromJson(Server.getMusicianTracks(mAndyRocksIdJson), trackSetType)),
                () -> assertEquals(Set.of(tJustice),
                        gson.fromJson(Server.getMusicianTracks(mMiaAmoIdJson), trackSetType)),
                () -> assertEquals(Set.of(tJustice, tSketch),
                        gson.fromJson(Server.getMusicianTracks(mEmmaBreakIdJson), trackSetType)),
                () -> assertEquals(Set.of(),
                        gson.fromJson(Server.getMusicianTracks(mChrisEagleIdJson), trackSetType)),
                
                () -> assertEquals(Set.of(mAndyRocks),
                        gson.fromJson(Server.getTrackMusicians(tAloneIdJson), musicianSetType)),
                () -> assertEquals(Set.of(mAndyRocks, mMiaAmo, mEmmaBreak),
                        gson.fromJson(Server.getTrackMusicians(tJusticeIdJson), musicianSetType)),
                () -> assertEquals(Set.of(),
                        gson.fromJson(Server.getTrackMusicians(tThePoisonIdJson), musicianSetType)),
                () -> assertEquals(Set.of(mAndyRocks, mEmmaBreak),
                        gson.fromJson(Server.getTrackMusicians(tSketchIdJson), musicianSetType))
        );
    }
    
    
    @Test
    void testAssociateMusiciansWithTrackBadRequests() {
        String mAndyRocksIdJson = Server.addMusician(gson.toJson(mAndyRocks));
        String tJusticeIdJson = Server.addTrack(gson.toJson(tJustice));
        
        int mAndyRocksId = gson.fromJson(mAndyRocksIdJson, IdDto.class).getId();
        int tJusticeId = gson.fromJson(tJusticeIdJson, IdDto.class).getId();
        
        String wrongMusician1 = gson.fromJson(
                Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tJusticeId, List.of(mAndyRocksId + 1)))), ErrorDto.class).getError();
        String wrongMusician2 = gson.fromJson(
                Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(
                                tJusticeId, List.of(mAndyRocksId, mAndyRocksId + 1)))), ErrorDto.class).getError();
        String wrongTrack = gson.fromJson(
                Server.associateMusiciansWithTrack(gson.toJson(
                        new TrackIdMusicianIdsDto(tJusticeId + 1, List.of(mAndyRocksId)))), ErrorDto.class).getError();
        String syntax = gson.fromJson(
                Server.associateMusiciansWithTrack("/"), ErrorDto.class).getError();
        String empty = gson.fromJson(
                Server.associateMusiciansWithTrack("  "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(
                Server.associateMusiciansWithTrack("{'value':0}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_MUSICIAN_NOT_FOUND, wrongMusician1),
                () -> assertEquals(ErrorMessage.DB_MUSICIAN_NOT_FOUND, wrongMusician2),
                () -> assertEquals(ErrorMessage.DB_TRACK_NOT_FOUND, wrongTrack),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
    
    
    @Test
    void testGetMusicianTracksBadRequests() {
        String wrongMusician = gson.fromJson(
                Server.getMusicianTracks(gson.toJson(new IdDto(1))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.getMusicianTracks("-"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.getMusicianTracks(" "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.getMusicianTracks("{'value':0}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_MUSICIAN_NOT_FOUND, wrongMusician),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
    
    
    @Test
    void testGetTrackMusiciansBadRequests() {
        String wrongTrack = gson.fromJson(
                Server.getTrackMusicians(gson.toJson(new IdDto(1))), ErrorDto.class).getError();
        String syntax = gson.fromJson(Server.getTrackMusicians("-"), ErrorDto.class).getError();
        String empty = gson.fromJson(Server.getTrackMusicians(" "), ErrorDto.class).getError();
        String wrongField = gson.fromJson(Server.getTrackMusicians("{'value':0}"), ErrorDto.class).getError();
        
        assertAll(
                () -> assertEquals(ErrorMessage.DB_TRACK_NOT_FOUND, wrongTrack),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, syntax),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, empty),
                () -> assertEquals(ErrorMessage.REQUEST_FORMAT_EXCEPTION, wrongField)
        );
    }
}
