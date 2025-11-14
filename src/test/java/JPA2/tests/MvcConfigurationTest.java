package JPA2.tests;

import JPA2.config.MvcConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.junit.Assert.*;

/**
 * Comprehensive tests for MvcConfiguration
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@WebAppConfiguration
public class MvcConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testMvcConfigurationInstantiation() {
        MvcConfiguration config = new MvcConfiguration();
        assertNotNull(config);
    }

    @Test
    public void testViewResolverBean() {
        MvcConfiguration config = new MvcConfiguration();
        ViewResolver viewResolver = config.getViewResolver();

        assertNotNull(viewResolver);
        assertTrue(viewResolver instanceof InternalResourceViewResolver);
    }

    @Test
    public void testViewResolverConfiguration() {
        MvcConfiguration config = new MvcConfiguration();
        ViewResolver viewResolver = config.getViewResolver();

        assertTrue(viewResolver instanceof InternalResourceViewResolver);
        InternalResourceViewResolver resolver = (InternalResourceViewResolver) viewResolver;

        assertEquals("/WEB-INF/views/", resolver.getPrefix());
        assertEquals(".jsp", resolver.getSuffix());
    }

    @Test
    public void testViewResolverPrefixSuffix() {
        MvcConfiguration config = new MvcConfiguration();
        InternalResourceViewResolver resolver = (InternalResourceViewResolver) config.getViewResolver();

        String prefix = resolver.getPrefix();
        String suffix = resolver.getSuffix();

        assertEquals("/WEB-INF/views/", prefix);
        assertEquals(".jsp", suffix);

        // Test view name resolution
        assertNotNull(prefix);
        assertNotNull(suffix);
    }

    @Test
    public void testAddResourceHandlers() {
        MvcConfiguration config = new MvcConfiguration();
        ResourceHandlerRegistry registry = new ResourceHandlerRegistry(
            applicationContext,
            null
        );

        // This tests that the method can be called without exceptions
        config.addResourceHandlers(registry);

        // Verify the method doesn't throw exceptions
        assertNotNull(config);
    }

    @Test
    public void testResourceHandlerConfiguration() {
        MvcConfiguration config = new MvcConfiguration();

        // Create a test registry
        ResourceHandlerRegistry registry = new ResourceHandlerRegistry(
            applicationContext,
            null
        );

        // Call the method under test
        config.addResourceHandlers(registry);

        // Verify no exceptions were thrown
        assertNotNull(registry);
    }

    @Test
    public void testMultipleViewResolverCreation() {
        MvcConfiguration config = new MvcConfiguration();

        ViewResolver resolver1 = config.getViewResolver();
        ViewResolver resolver2 = config.getViewResolver();

        // Each call should create a new instance
        assertNotNull(resolver1);
        assertNotNull(resolver2);
        assertNotSame(resolver1, resolver2);
    }

    @Test
    public void testViewResolverType() {
        MvcConfiguration config = new MvcConfiguration();
        ViewResolver viewResolver = config.getViewResolver();

        assertEquals(InternalResourceViewResolver.class, viewResolver.getClass());
    }

    @Test
    public void testConfigurationComponentScan() {
        // This test verifies that the configuration class exists and can be instantiated
        MvcConfiguration config = new MvcConfiguration();

        assertNotNull(config);

        // Verify that ViewResolver can be created
        ViewResolver viewResolver = config.getViewResolver();
        assertNotNull(viewResolver);
    }
}
