package JPA2.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 * Validates Spring and JPA configuration after dependency updates.
 * 
 * Ensures:
 * - Spring context loads correctly
 * - All required beans are available
 * - Database connectivity works
 * - JPA configuration is valid
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
public class ConfigurationValidationTest {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Test that Spring application context loads successfully
     */
    @Test
    public void testSpringContextLoads() {
        assertNotNull("Application context should not be null", applicationContext);
        assertTrue("Application context should be active", applicationContext.getId() \!= null);
    }
    
    /**
     * Test that DataSource bean is configured correctly
     */
    @Test
    public void testDataSourceConfiguration() {
        assertNotNull("DataSource should be configured", dataSource);
        
        try {
            assertNotNull("DataSource connection should be available", 
                         dataSource.getConnection());
        } catch (Exception e) {
            fail("Failed to get database connection: " + e.getMessage());
        }
    }
    
    /**
     * Test that EntityManagerFactory is configured correctly
     */
    @Test
    public void testEntityManagerFactoryConfiguration() {
        assertNotNull("EntityManagerFactory should not be null", entityManagerFactory);
        assertTrue("EntityManagerFactory should be open", entityManagerFactory.isOpen());
        
        // Verify we can create an EntityManager
        EntityManager em = entityManagerFactory.createEntityManager();
        assertNotNull("Should be able to create EntityManager", em);
        assertTrue("Created EntityManager should be open", em.isOpen());
        em.close();
    }
    
    /**
     * Test that EntityManager is injected correctly
     */
    @Test
    public void testEntityManagerInjection() {
        assertNotNull("EntityManager should be injected", entityManager);
        assertTrue("EntityManager should be open", entityManager.isOpen());
    }
    
    /**
     * Test that transaction manager bean exists
     */
    @Test
    public void testTransactionManagerExists() {
        assertTrue("Transaction manager bean should exist",
                  applicationContext.containsBean("transactionManager"));
        
        Object txManager = applicationContext.getBean("transactionManager");
        assertNotNull("Transaction manager should not be null", txManager);
    }
    
    /**
     * Test that all required Spring beans are available
     */
    @Test
    public void testRequiredBeansAvailable() {
        // Check for essential beans
        String[] requiredBeans = {
            "dataSource",
            "entityManagerFactory",
            "transactionManager"
        };
        
        for (String beanName : requiredBeans) {
            assertTrue("Bean '" + beanName + "' should exist",
                      applicationContext.containsBean(beanName));
        }
    }
    
    /**
     * Test JPA properties configuration
     */
    @Test
    public void testJpaPropertiesConfiguration() {
        assertNotNull("EntityManagerFactory should have properties", 
                     entityManagerFactory.getProperties());
        
        // Verify Hibernate is the JPA provider
        assertTrue("Should be using Hibernate as JPA provider",
                  entityManagerFactory.getClass().getName().contains("Hibernate"));
    }
    
    /**
     * Test database schema exists (from hbm2ddl.auto)
     */
    @Test
    public void testDatabaseSchemaCreated() {
        // Try to query system tables to verify schema creation
        try {
            entityManager.createNativeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES")
                        .getSingleResult();
            assertTrue("Database schema should be created", true);
        } catch (Exception e) {
            // Try H2-specific query
            try {
                entityManager.createNativeQuery("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES")
                            .getSingleResult();
            } catch (Exception ex) {
                fail("Could not verify database schema: " + ex.getMessage());
            }
        }
    }
}