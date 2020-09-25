package music_storage;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


public class TestServer {
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
    
    }
    
    
    @Test
    void testSmth() {
    
    }
}
