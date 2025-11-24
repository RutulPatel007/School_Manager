package com.schoolmanagement.tests;

import com.schoolmanagement.dao.LibraryDAO;
import com.schoolmanagement.models.Library;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryDAOTest {

    private static Connection connection;
    private static LibraryDAO libraryDAO;
    
    // Assumed unique ID for setup/teardown consistency
    private static final int SETUP_LIBRARY_ID = 999; 

    // --- Setup and Teardown ---

    @BeforeAll
    static void setupDatabase() throws SQLException {
        // NOTE: Replace with your actual database connection details if necessary
        connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/school_db?useSSL=false&allowPublicKeyRetrieval=true",
            "root",
            "admin"
        );
        libraryDAO = new LibraryDAO(connection);
        
        // Ensure a known library ID exists if foreign keys are involved later
        try (Statement st = connection.createStatement()) {
            st.execute("INSERT IGNORE INTO libraries (id, name) VALUES (" + SETUP_LIBRARY_ID + ", 'Permanent Library')");
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Clean up the setup data before closing
        try (Statement st = connection.createStatement()) {
             st.execute("DELETE FROM books WHERE library_id = " + SETUP_LIBRARY_ID);
             st.execute("DELETE FROM libraries WHERE id = " + SETUP_LIBRARY_ID);
        }
        if (connection != null) connection.close();
    }

    @BeforeEach
    void cleanTablesBefore() throws SQLException {
        // Clear test data added in previous runs (excluding the permanent setup ID)
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM libraries WHERE id <> " + SETUP_LIBRARY_ID);
        }
    }
    
    // --- Utility Method to Get ID ---
    // Used to bridge the DAO operations back to the database state
    private int fetchIdByName(String name) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT id FROM libraries WHERE name = ?"
        )) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        }
        return -1;
    }


    // --- Test Cases ---

    @Test
    @Order(1)
    void testCreateReadDeleteCycle() throws SQLException {
        final String TEST_NAME = "Test Library A";
        
        // 1. CREATE (Covers L17-L21)
        Library l1 = new Library(0, TEST_NAME); // ID 0 is a placeholder for unpersisted object
        libraryDAO.create(l1);

        // Fetch ID to verify insertion
        int generatedId = fetchIdByName(TEST_NAME);
        assertTrue(generatedId > 0, "Library should be inserted.");

        // 2. READ & mapResultSetToEntity (Covers L25-L31, L43-L47 and all related mutants)
        Library readLibrary = libraryDAO.read(generatedId);
        assertNotNull(readLibrary);
        assertEquals(generatedId, readLibrary.getId());
        assertEquals(TEST_NAME, readLibrary.getName());

        // 3. DELETE (Covers L35-L39)
        libraryDAO.delete(generatedId);
        assertNull(libraryDAO.read(generatedId), "Library should be deleted and read should return null.");
    }
    
    @Test
    @Order(2)
    void testUpdateNameAndReadNonExistent() throws SQLException {
        final String OLD_NAME = "Library Old Name";
        final String NEW_NAME = "Library New Name";
        
        // ARRANGE: Insert a library directly to ensure it has an ID
        libraryDAO.create(new Library(0, OLD_NAME));
        int id = fetchIdByName(OLD_NAME);
        assertTrue(id > 0);

        // ACT: Update via DAO (Covers L61-L65)
        libraryDAO.update(id, NEW_NAME);

        // ASSERT: Read back and verify change
        Library updated = libraryDAO.read(id);
        assertNotNull(updated);
        assertEquals(NEW_NAME, updated.getName());
        
        // ASSERT: Read non-existent ID (Kills read L28 mutants for false condition)
        assertNull(libraryDAO.read(-1), "Reading a non-existent ID should return null.");
    }

    @Test
    @Order(3)
    void testListMappingAndGetAllLibraries() throws SQLException {
        // 1. Test GetAllLibraries with existing setup data (Covers L57)
        List<Library> initialList = libraryDAO.getAllLibraries();
        assertTrue(initialList.size() >= 1, "Should contain the initial permanent library.");

        // ARRANGE: Insert test data
        libraryDAO.create(new Library(0, "North Campus"));
        libraryDAO.create(new Library(0, "South Campus"));

        // 2. Test Non-Empty List (Covers L52 Conditional for multiple rows)
        List<Library> populatedList = libraryDAO.getAllLibraries();
        assertTrue(populatedList.size() >= 3, "List should contain at least three items.");
        
        // 3. Test Empty ResultSet Mapping (Covers L52 Conditional for no rows & L53 return mutant)
        ResultSet emptyRs = connection.createStatement().executeQuery("SELECT * FROM libraries WHERE id = -1");
        List<Library> emptyList = libraryDAO.mapResultSetToList(emptyRs);
        assertNotNull(emptyList);
        assertEquals(0, emptyList.size(), "Mapping an empty result set should return an empty list.");
    }
}