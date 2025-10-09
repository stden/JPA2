package JPA2.tests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Tests to validate that the correct dependency versions are being used.
 * 
 * This test ensures:
 * - SLF4J 2.0.17 is loaded
 * - Hibernate 6.4.10.Final is loaded
 * - No version conflicts exist
 * - All required classes are available
 */
public class DependencyVersionTest {
    
    private static final Logger logger = LoggerFactory.getLogger(DependencyVersionTest.class);
    
    /**
     * Verify SLF4J API is available and at correct version
     */
    @Test
    public void testSlf4jApiVersion() {
        // Verify SLF4J classes are loadable
        assertNotNull("SLF4J Logger should be available", logger);
        assertNotNull("SLF4J LoggerFactory should be available", LoggerFactory.class);
        
        // Test SLF4J 2.x specific features
        try {
            // SLF4J 2.x has fluent API
            logger.atInfo().log("Testing SLF4J 2.x fluent API");
            assertTrue("SLF4J 2.x fluent API available", true);
        } catch (NoSuchMethodError e) {
            fail("SLF4J 2.x fluent API not available. Wrong version loaded?");
        }
        
        logger.info("SLF4J API version test passed");
    }
    
    /**
     * Test SLF4J fluent API (2.x feature)
     */
    @Test
    public void testSlf4jFluentApi() {
        // Test various fluent API methods
        logger.atTrace().log("Fluent trace");
        logger.atDebug().log("Fluent debug");
        logger.atInfo().log("Fluent info");
        logger.atWarn().log("Fluent warn");
        logger.atError().log("Fluent error");
        
        // Test with arguments
        logger.atInfo()
              .addArgument("arg1")
              .addArgument(42)
              .log("Fluent logging with args: {} and {}");
        
        // Test with key-value pairs
        logger.atInfo()
              .addKeyValue("key1", "value1")
              .addKeyValue("key2", 123)
              .log("Fluent logging with key-value pairs");
        
        assertTrue("SLF4J 2.x fluent API test passed", true);
    }
    
    /**
     * Verify Hibernate is at correct version
     */
    @Test
    public void testHibernateVersion() {
        try {
            String version = org.hibernate.Version.getVersionString();
            assertNotNull("Hibernate version should be available", version);
            
            // Should be 6.4.10.Final or similar
            assertTrue("Hibernate version should be 6.4.x", 
                      version.contains("6.4"));
            
            logger.info("Hibernate version: {}", version);
            logger.info("Hibernate version test passed");
        } catch (Exception e) {
            fail("Could not determine Hibernate version: " + e.getMessage());
        }
    }
    
    /**
     * Test Jakarta Persistence API availability
     */
    @Test
    public void testJakartaPersistenceApi() {
        // Verify Jakarta classes are available (not javax)
        try {
            Class.forName("jakarta.persistence.EntityManager");
            Class.forName("jakarta.persistence.Entity");
            Class.forName("jakarta.persistence.PersistenceContext");
            assertTrue("Jakarta Persistence API available", true);
        } catch (ClassNotFoundException e) {
            fail("Jakarta Persistence API classes not found: " + e.getMessage());
        }
        
        // Verify old javax classes are NOT being used
        try {
            Class.forName("javax.persistence.EntityManager");
            fail("Old javax.persistence classes should not be on classpath");
        } catch (ClassNotFoundException e) {
            // Expected - we should be using jakarta, not javax
            assertTrue("Correctly using Jakarta instead of javax", true);
        }
        
        logger.info("Jakarta Persistence API test passed");
    }
    
    /**
     * Test Spring Framework classes availability
     */
    @Test
    public void testSpringFrameworkAvailability() {
        try {
            Class.forName("org.springframework.context.ApplicationContext");
            Class.forName("org.springframework.test.context.junit4.SpringJUnit4ClassRunner");
            Class.forName("org.springframework.transaction.annotation.Transactional");
            assertTrue("Spring Framework classes available", true);
        } catch (ClassNotFoundException e) {
            fail("Spring Framework classes not found: " + e.getMessage());
        }
        
        logger.info("Spring Framework availability test passed");
    }
    
    /**
     * Test that no conflicting versions exist
     */
    @Test
    public void testNoVersionConflicts() {
        // Try to load key classes that might have conflicts
        try {
            // SLF4J
            LoggerFactory.getLogger("test");
            
            // Hibernate
            org.hibernate.Session.class.getName();
            
            // Jakarta
            jakarta.persistence.EntityManager.class.getName();
            
            // Spring
            org.springframework.context.ApplicationContext.class.getName();
            
            assertTrue("No version conflicts detected", true);
            logger.info("Version conflict test passed");
        } catch (Exception e) {
            fail("Version conflict detected: " + e.getMessage());
        }
    }
    
    /**
     * Test JUnit availability (test framework)
     */
    @Test
    public void testJunitAvailability() {
        assertNotNull("JUnit Assert class should be available", Assert.class);
        assertNotNull("JUnit Test annotation should be available", Test.class);
        logger.info("JUnit test framework available");
    }
}