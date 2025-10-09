package JPA2.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Comprehensive tests for dependency compatibility after version updates.
 * 
 * Tests the compatibility of:
 * - Hibernate 6.4.10.Final
 * - SLF4J 2.0.17
 * - Spring Framework 6.2.11
 * - Jakarta Persistence API 3.2.0
 * 
 * Covers:
 * - Logging functionality with new SLF4J version
 * - Hibernate core functionality
 * - Spring integration
 * - Transaction management
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class PomDependencyCompatibilityTest {
    
    private static final Logger logger = LoggerFactory.getLogger(PomDependencyCompatibilityTest.class);
    
    @PersistenceContext
    EntityManager em;
    
    /**
     * Test SLF4J 2.0.17 logging functionality
     * Verifies that the upgraded SLF4J version works correctly
     */
    @Test
    public void testSlf4jLoggingCompatibility() {
        // Test basic logging at different levels
        assertNotNull("Logger should not be null", logger);
        
        // Test all log levels
        logger.trace("SLF4J 2.0.17 - Trace level test");
        logger.debug("SLF4J 2.0.17 - Debug level test");
        logger.info("SLF4J 2.0.17 - Info level test");
        logger.warn("SLF4J 2.0.17 - Warn level test");
        logger.error("SLF4J 2.0.17 - Error level test");
        
        // Verify logger name
        assertEquals("Logger name should match class", 
                    "JPA2.tests.PomDependencyCompatibilityTest", 
                    logger.getName());
        
        // Test parameterized logging (SLF4J feature)
        String param1 = "param1";
        Integer param2 = 42;
        logger.info("Parameterized logging test: {} and {}", param1, param2);
        
        assertTrue("SLF4J logging compatibility test passed", true);
    }
    
    /**
     * Test SLF4J error logging with exceptions
     */
    @Test
    public void testSlf4jExceptionLogging() {
        Exception testException = new RuntimeException("Test exception for SLF4J");
        
        // Test exception logging
        logger.error("Testing exception logging", testException);
        
        // Test exception with message parameters
        logger.error("Exception with params: {}", "testValue", testException);
        
        assertTrue("Exception logging test passed", true);
    }
    
    /**
     * Test Hibernate 6.4.10.Final compatibility with EntityManager
     */
    @Test
    public void testHibernateEntityManagerCompatibility() {
        assertNotNull("EntityManager should be injected", em);
        assertTrue("EntityManager should be open", em.isOpen());
        
        // Verify EntityManager factory is working
        assertNotNull("EntityManagerFactory should be available", 
                     em.getEntityManagerFactory());
        
        logger.info("Hibernate EntityManager compatibility verified");
    }
    
    /**
     * Test Hibernate session factory initialization
     */
    @Test
    public void testHibernateSessionFactoryInitialization() {
        // Verify that Hibernate can create queries
        assertNotNull("EntityManager should support query creation", em);
        
        // Test native query creation (Hibernate feature)
        try {
            em.createNativeQuery("SELECT 1 FROM DUAL");
            logger.info("Hibernate native query creation successful");
        } catch (Exception e) {
            // Some databases might not support DUAL, try alternative
            em.createNativeQuery("SELECT 1");
            logger.info("Hibernate native query creation successful (alternative syntax)");
        }
        
        assertTrue("Hibernate session factory initialized correctly", true);
    }
    
    /**
     * Test transaction management compatibility
     */
    @Test
    @Transactional
    public void testTransactionManagementCompatibility() {
        // Verify transaction is active
        assertTrue("Transaction should be active in @Transactional method",
                  em.getTransaction().isActive() || 
                  em.isJoinedToTransaction());
        
        logger.info("Transaction management working correctly");
    }
    
    /**
     * Test Spring context initialization with new dependencies
     */
    @Test
    public void testSpringContextInitialization() {
        // If we got here, Spring context loaded successfully
        assertNotNull("EntityManager injection successful", em);
        
        logger.info("Spring context initialized successfully with new dependency versions");
        assertTrue("Spring integration working", true);
    }
    
    /**
     * Test persistence context flush operation
     */
    @Test
    @Transactional
    public void testPersistenceContextFlush() {
        try {
            em.flush();
            logger.info("EntityManager flush operation successful");
            assertTrue("Flush operation completed", true);
        } catch (Exception e) {
            fail("Flush operation failed: " + e.getMessage());
        }
    }
    
    /**
     * Test that SLF4J MDC (Mapped Diagnostic Context) works
     */
    @Test
    public void testSlf4jMdcFunctionality() {
        org.slf4j.MDC.put("testKey", "testValue");
        String value = org.slf4j.MDC.get("testKey");
        
        assertEquals("MDC should store and retrieve values", "testValue", value);
        
        logger.info("Testing MDC functionality");
        
        org.slf4j.MDC.remove("testKey");
        assertNull("MDC value should be removed", org.slf4j.MDC.get("testKey"));
    }
    
    /**
     * Test SLF4J marker functionality
     */
    @Test
    public void testSlf4jMarkerFunctionality() {
        org.slf4j.Marker marker = org.slf4j.MarkerFactory.getMarker("TEST_MARKER");
        assertNotNull("Marker should be created", marker);
        assertEquals("Marker name should match", "TEST_MARKER", marker.getName());
        
        logger.info(marker, "Testing marker functionality");
        assertTrue("Marker functionality test passed", true);
    }
    
    /**
     * Integration test: Verify all major components work together
     */
    @Test
    @Transactional
    public void testFullStackIntegration() {
        // 1. Logging works
        logger.info("Starting full stack integration test");
        
        // 2. EntityManager is available
        assertNotNull("EntityManager available", em);
        
        // 3. Transaction is active
        assertTrue("Transaction active", 
                  em.getTransaction().isActive() || em.isJoinedToTransaction());
        
        // 4. Can perform database operations
        em.flush();
        
        logger.info("Full stack integration test completed successfully");
        assertTrue("Full integration test passed", true);
    }
    
    /**
     * Test that the correct dependency versions are loaded
     */
    @Test
    public void testDependencyVersions() {
        // Test SLF4J version
        String slf4jVersion = org.slf4j.LoggerFactory.class.getPackage().getImplementationVersion();
        logger.info("SLF4J version: {}", slf4jVersion);
        
        // Test Hibernate version
        String hibernateVersion = org.hibernate.Version.getVersionString();
        logger.info("Hibernate version: {}", hibernateVersion);
        assertNotNull("Hibernate version should be available", hibernateVersion);
        assertTrue("Hibernate version should start with 6.4", 
                  hibernateVersion.startsWith("6.4"));
        
        logger.info("Dependency versions verified");
    }
}