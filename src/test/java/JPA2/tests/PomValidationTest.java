package JPA2.tests;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Comprehensive validation tests for pom.xml configuration file.
 * Tests validate XML structure, dependencies, versions, properties, and Maven configuration.
 */
public class PomValidationTest {

    private Document pomDocument;
    private Element projectElement;
    private static final String POM_FILE_PATH = "pom.xml";
    
    // Version pattern for semantic versioning
    private static final Pattern VERSION_PATTERN = Pattern.compile(
        "^\\d+\\.\\d+(\\.\\d+)?(\\.\\w+)?(-\\w+)?$"
    );
    
    // Property reference pattern ${property.name}
    private static final Pattern PROPERTY_REF_PATTERN = Pattern.compile(
        "^\\$\\{[a-zA-Z0-9._-]+\\}$"
    );

    @Before
    public void setUp() throws ParserConfigurationException, IOException, SAXException {
        File pomFile = new File(POM_FILE_PATH);
        assertTrue("pom.xml file must exist", pomFile.exists());
        assertTrue("pom.xml must be readable", pomFile.canRead());
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        pomDocument = builder.parse(pomFile);
        pomDocument.getDocumentElement().normalize();
        
        projectElement = pomDocument.getDocumentElement();
        assertEquals("Root element must be 'project'", "project", projectElement.getNodeName());
    }

    /**
     * Test that validates basic Maven coordinates are properly defined
     */
    @Test
    public void testMavenCoordinatesArePresent() {
        assertNotNull("GroupId must be present", getElementValue("groupId"));
        assertNotNull("ArtifactId must be present", getElementValue("artifactId"));
        assertNotNull("Version must be present", getElementValue("version"));
        
        assertEquals("GroupId should be JPA2", "JPA2", getElementValue("groupId"));
        assertEquals("ArtifactId should be JPA2", "JPA2", getElementValue("artifactId"));
        assertFalse("Version should not be empty", getElementValue("version").trim().isEmpty());
    }

    /**
     * Test that validates modelVersion is correctly specified
     */
    @Test
    public void testModelVersionIsValid() {
        String modelVersion = getElementValue("modelVersion");
        assertNotNull("ModelVersion must be present", modelVersion);
        assertEquals("ModelVersion should be 4.0.0", "4.0.0", modelVersion);
    }

    /**
     * Test that validates all required properties are defined
     */
    @Test
    public void testRequiredPropertiesExist() {
        Map<String, String> properties = getProperties();
        
        assertTrue("project.build.sourceEncoding property must exist", 
            properties.containsKey("project.build.sourceEncoding"));
        assertTrue("spring.version property must exist", 
            properties.containsKey("spring.version"));
        assertTrue("hibernate.version property must exist", 
            properties.containsKey("hibernate.version"));
        assertTrue("org.slf4j-version property must exist", 
            properties.containsKey("org.slf4j-version"));
    }

    /**
     * Test that validates property values are in correct format
     */
    @Test
    public void testPropertyValuesAreValid() {
        Map<String, String> properties = getProperties();
        
        String encoding = properties.get("project.build.sourceEncoding");
        assertEquals("Encoding should be UTF-8", "UTF-8", encoding);
        
        String springVersion = properties.get("spring.version");
        assertNotNull("Spring version must not be null", springVersion);
        assertTrue("Spring version must match version pattern: " + springVersion,
            VERSION_PATTERN.matcher(springVersion).matches());
        
        String hibernateVersion = properties.get("hibernate.version");
        assertNotNull("Hibernate version must not be null", hibernateVersion);
        assertTrue("Hibernate version must match version pattern: " + hibernateVersion,
            VERSION_PATTERN.matcher(hibernateVersion).matches());
        
        String slf4jVersion = properties.get("org.slf4j-version");
        assertNotNull("SLF4J version must not be null", slf4jVersion);
        assertTrue("SLF4J version must match version pattern: " + slf4jVersion,
            VERSION_PATTERN.matcher(slf4jVersion).matches());
    }

    /**
     * Test that validates specific version numbers from the latest changes
     */
    @Test
    public void testSpecificVersionNumbers() {
        Map<String, String> properties = getProperties();
        
        String hibernateVersion = properties.get("hibernate.version");
        assertEquals("Hibernate version should be 6.4.10.Final", "6.4.10.Final", hibernateVersion);
        
        String slf4jVersion = properties.get("org.slf4j-version");
        assertEquals("SLF4J version should be 2.0.17", "2.0.17", slf4jVersion);
        
        String springVersion = properties.get("spring.version");
        assertEquals("Spring version should be 6.2.11", "6.2.11", springVersion);
    }

    /**
     * Test that validates all critical dependencies are present
     */
    @Test
    public void testCriticalDependenciesExist() {
        List<Dependency> dependencies = getDependencies();
        
        assertTrue("Spring context dependency must exist",
            hasDependency(dependencies, "org.springframework", "spring-context"));
        assertTrue("Spring WebMVC dependency must exist",
            hasDependency(dependencies, "org.springframework", "spring-webmvc"));
        assertTrue("Spring ORM dependency must exist",
            hasDependency(dependencies, "org.springframework", "spring-orm"));
        assertTrue("Spring Test dependency must exist",
            hasDependency(dependencies, "org.springframework", "spring-test"));
        assertTrue("Hibernate Core dependency must exist",
            hasDependency(dependencies, "org.hibernate.orm", "hibernate-core"));
        assertTrue("JUnit dependency must exist",
            hasDependency(dependencies, "junit", "junit"));
        assertTrue("JPA API dependency must exist",
            hasDependency(dependencies, "jakarta.persistence", "jakarta.persistence-api"));
        assertTrue("SLF4J API dependency must exist",
            hasDependency(dependencies, "org.slf4j", "slf4j-api"));
    }

    /**
     * Test that validates dependency scopes are properly set
     */
    @Test
    public void testDependencyScopes() {
        List<Dependency> dependencies = getDependencies();
        
        // Test scope must be explicitly set
        Dependency springTest = findDependency(dependencies, "org.springframework", "spring-test");
        assertNotNull("Spring Test dependency should exist", springTest);
        assertEquals("Spring Test should have test scope", "test", springTest.scope);
        
        Dependency junit = findDependency(dependencies, "junit", "junit");
        assertNotNull("JUnit dependency should exist", junit);
        assertEquals("JUnit should have test scope", "test", junit.scope);
        
        Dependency hsqldb = findDependency(dependencies, "org.hsqldb", "hsqldb");
        assertNotNull("HSQLDB dependency should exist", hsqldb);
        assertEquals("HSQLDB should have test scope", "test", hsqldb.scope);
        
        // Runtime scope
        Dependency h2 = findDependency(dependencies, "com.h2database", "h2");
        assertNotNull("H2 dependency should exist", h2);
        assertEquals("H2 should have runtime scope", "runtime", h2.scope);
        
        // Provided scope
        Dependency servlet = findDependency(dependencies, "jakarta.servlet", "jakarta.servlet-api");
        assertNotNull("Servlet API dependency should exist", servlet);
        assertEquals("Servlet API should have provided scope", "provided", servlet.scope);
    }

    /**
     * Test that validates versions use property references where appropriate
     */
    @Test
    public void testDependencyVersionsUseProperties() {
        List<Dependency> dependencies = getDependencies();
        
        // Spring dependencies should use ${spring.version}
        Dependency springContext = findDependency(dependencies, "org.springframework", "spring-context");
        assertEquals("Spring context should use version property", 
            "${spring.version}", springContext.version);
        
        Dependency springWebMvc = findDependency(dependencies, "org.springframework", "spring-webmvc");
        assertEquals("Spring webmvc should use version property", 
            "${spring.version}", springWebMvc.version);
        
        // Hibernate should use ${hibernate.version}
        Dependency hibernate = findDependency(dependencies, "org.hibernate.orm", "hibernate-core");
        assertEquals("Hibernate should use version property", 
            "${hibernate.version}", hibernate.version);
        
        // SLF4J dependencies should use ${org.slf4j-version}
        Dependency slf4jApi = findDependency(dependencies, "org.slf4j", "slf4j-api");
        assertEquals("SLF4J API should use version property", 
            "${org.slf4j-version}", slf4jApi.version);
    }

    /**
     * Test that validates there are no duplicate dependencies
     */
    @Test
    public void testNoDuplicateDependencies() {
        List<Dependency> dependencies = getDependencies();
        Set<String> uniqueDeps = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        
        for (Dependency dep : dependencies) {
            String key = dep.groupId + ":" + dep.artifactId;
            if (\!uniqueDeps.add(key)) {
                duplicates.add(key);
            }
        }
        
        assertTrue("No duplicate dependencies should exist: " + duplicates, 
            duplicates.isEmpty());
    }

    /**
     * Test that validates Maven compiler plugin configuration
     */
    @Test
    public void testCompilerPluginConfiguration() {
        List<Plugin> plugins = getPlugins();
        
        Plugin compilerPlugin = findPlugin(plugins, "org.apache.maven.plugins", "maven-compiler-plugin");
        assertNotNull("Maven compiler plugin must be configured", compilerPlugin);
        assertNotNull("Compiler plugin version must be specified", compilerPlugin.version);
        assertTrue("Compiler plugin version should not be empty", 
            \!compilerPlugin.version.trim().isEmpty());
    }

    /**
     * Test that validates Maven surefire plugin for test execution
     */
    @Test
    public void testSurefirePluginConfiguration() {
        List<Plugin> plugins = getPlugins();
        
        Plugin surefirePlugin = findPlugin(plugins, "org.apache.maven.plugins", "maven-surefire-plugin");
        assertNotNull("Maven surefire plugin must be configured", surefirePlugin);
        assertNotNull("Surefire plugin version must be specified", surefirePlugin.version);
    }

    /**
     * Test that validates Java version compatibility
     */
    @Test
    public void testJavaVersionCompatibility() {
        List<Plugin> plugins = getPlugins();
        Plugin compilerPlugin = findPlugin(plugins, "org.apache.maven.plugins", "maven-compiler-plugin");
        
        assertNotNull("Compiler plugin must exist", compilerPlugin);
        
        // Check configuration for Java version
        Element configElement = compilerPlugin.configurationElement;
        if (configElement \!= null) {
            String source = getChildElementValue(configElement, "source");
            String target = getChildElementValue(configElement, "target");
            
            if (source \!= null && target \!= null) {
                assertEquals("Source and target Java versions should match", source, target);
                assertTrue("Java version should be 11 or higher", 
                    Integer.parseInt(source) >= 11);
            }
        }
    }

    /**
     * Test that validates Spring and Hibernate version compatibility
     */
    @Test
    public void testSpringHibernateCompatibility() {
        Map<String, String> properties = getProperties();
        
        String springVersion = properties.get("spring.version");
        String hibernateVersion = properties.get("hibernate.version");
        
        // Spring 6.x requires Hibernate 6.x
        if (springVersion \!= null && springVersion.startsWith("6.")) {
            assertTrue("Spring 6.x requires Hibernate 6.x, found: " + hibernateVersion,
                hibernateVersion \!= null && hibernateVersion.startsWith("6."));
        }
    }

    /**
     * Test that validates Spring Data JPA compatibility
     */
    @Test
    public void testSpringDataJpaCompatibility() {
        List<Dependency> dependencies = getDependencies();
        Map<String, String> properties = getProperties();
        
        Dependency springDataJpa = findDependency(dependencies, 
            "org.springframework.data", "spring-data-jpa");
        assertNotNull("Spring Data JPA dependency should exist", springDataJpa);
        
        String springVersion = properties.get("spring.version");
        // Spring Data JPA 3.x requires Spring Framework 6.x
        if (springDataJpa.version \!= null && springDataJpa.version.startsWith("3.")) {
            assertTrue("Spring Data JPA 3.x requires Spring 6.x",
                springVersion \!= null && springVersion.startsWith("6."));
        }
    }

    /**
     * Test that validates all SLF4J dependencies use the same version
     */
    @Test
    public void testSlf4jVersionConsistency() {
        List<Dependency> dependencies = getDependencies();
        List<Dependency> slf4jDeps = new ArrayList<>();
        
        for (Dependency dep : dependencies) {
            if ("org.slf4j".equals(dep.groupId)) {
                slf4jDeps.add(dep);
            }
        }
        
        assertTrue("Should have multiple SLF4J dependencies", slf4jDeps.size() > 1);
        
        String expectedVersion = "${org.slf4j-version}";
        for (Dependency dep : slf4jDeps) {
            assertEquals("All SLF4J dependencies should use same version property: " + 
                dep.artifactId, expectedVersion, dep.version);
        }
    }

    /**
     * Test that validates database driver dependencies
     */
    @Test
    public void testDatabaseDriverDependencies() {
        List<Dependency> dependencies = getDependencies();
        
        Dependency h2 = findDependency(dependencies, "com.h2database", "h2");
        assertNotNull("H2 database driver should be present", h2);
        assertEquals("H2 should have runtime scope", "runtime", h2.scope);
        
        Dependency hsqldb = findDependency(dependencies, "org.hsqldb", "hsqldb");
        assertNotNull("HSQLDB driver should be present", hsqldb);
        assertEquals("HSQLDB should have test scope", "test", hsqldb.scope);
    }

    /**
     * Test that validates Jakarta EE namespace migration
     */
    @Test
    public void testJakartaNamespaceUsage() {
        List<Dependency> dependencies = getDependencies();
        
        // Should use jakarta.persistence instead of javax.persistence
        Dependency jakartaPersistence = findDependency(dependencies, 
            "jakarta.persistence", "jakarta.persistence-api");
        assertNotNull("Jakarta Persistence API should be used", jakartaPersistence);
        
        // Should use jakarta.servlet instead of javax.servlet
        Dependency jakartaServlet = findDependency(dependencies, 
            "jakarta.servlet", "jakarta.servlet-api");
        assertNotNull("Jakarta Servlet API should be used", jakartaServlet);
        
        // Check that no javax.* dependencies exist
        for (Dependency dep : dependencies) {
            assertFalse("Should not use javax.persistence namespace", 
                "javax.persistence".equals(dep.groupId));
            assertFalse("Should not use javax.servlet namespace", 
                "javax.servlet".equals(dep.groupId));
        }
    }

    /**
     * Test that validates dependency version format consistency
     */
    @Test
    public void testDependencyVersionFormatConsistency() {
        List<Dependency> dependencies = getDependencies();
        
        for (Dependency dep : dependencies) {
            if (dep.version \!= null && \!dep.version.isEmpty()) {
                assertTrue("Version should be either a property reference or valid version: " + 
                    dep.groupId + ":" + dep.artifactId + ":" + dep.version,
                    PROPERTY_REF_PATTERN.matcher(dep.version).matches() || 
                    VERSION_PATTERN.matcher(dep.version).matches());
            }
        }
    }

    /**
     * Test that validates plugin version format consistency
     */
    @Test
    public void testPluginVersionFormatConsistency() {
        List<Plugin> plugins = getPlugins();
        
        for (Plugin plugin : plugins) {
            if (plugin.version \!= null && \!plugin.version.isEmpty()) {
                assertTrue("Plugin version should match version pattern: " + 
                    plugin.groupId + ":" + plugin.artifactId + ":" + plugin.version,
                    VERSION_PATTERN.matcher(plugin.version).matches());
            }
        }
    }

    /**
     * Test that validates all Spring Framework dependencies use the same version
     */
    @Test
    public void testSpringFrameworkVersionConsistency() {
        List<Dependency> dependencies = getDependencies();
        List<Dependency> springDeps = new ArrayList<>();
        
        for (Dependency dep : dependencies) {
            if ("org.springframework".equals(dep.groupId)) {
                springDeps.add(dep);
            }
        }
        
        assertTrue("Should have multiple Spring Framework dependencies", springDeps.size() > 1);
        
        String expectedVersion = "${spring.version}";
        for (Dependency dep : springDeps) {
            assertEquals("All Spring dependencies should use same version property: " + 
                dep.artifactId, expectedVersion, dep.version);
        }
    }

    /**
     * Test for minimum required dependencies count
     */
    @Test
    public void testMinimumDependenciesPresent() {
        List<Dependency> dependencies = getDependencies();
        assertTrue("Should have at least 10 dependencies", dependencies.size() >= 10);
    }

    /**
     * Test that validates XML structure is well-formed
     */
    @Test
    public void testXmlStructureWellFormed() {
        assertNotNull("POM document should be parsed successfully", pomDocument);
        assertNotNull("Project element should exist", projectElement);
        
        NodeList dependencies = projectElement.getElementsByTagName("dependencies");
        assertTrue("Dependencies section should exist", dependencies.getLength() > 0);
        
        NodeList properties = projectElement.getElementsByTagName("properties");
        assertTrue("Properties section should exist", properties.getLength() > 0);
        
        NodeList build = projectElement.getElementsByTagName("build");
        assertTrue("Build section should exist", build.getLength() > 0);
    }

    // Helper methods

    private String getElementValue(String tagName) {
        NodeList nodeList = projectElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                return node.getTextContent().trim();
            }
        }
        return null;
    }

    private String getChildElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return null;
    }

    private Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();
        NodeList propertiesList = projectElement.getElementsByTagName("properties");
        
        if (propertiesList.getLength() > 0) {
            Element propertiesElement = (Element) propertiesList.item(0);
            NodeList children = propertiesElement.getChildNodes();
            
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    props.put(node.getNodeName(), node.getTextContent().trim());
                }
            }
        }
        
        return props;
    }

    private List<Dependency> getDependencies() {
        List<Dependency> deps = new ArrayList<>();
        NodeList dependenciesList = projectElement.getElementsByTagName("dependencies");
        
        if (dependenciesList.getLength() > 0) {
            Element dependenciesElement = (Element) dependenciesList.item(0);
            NodeList dependencyNodes = dependenciesElement.getElementsByTagName("dependency");
            
            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                Element depElement = (Element) dependencyNodes.item(i);
                Dependency dep = new Dependency();
                dep.groupId = getChildElementValue(depElement, "groupId");
                dep.artifactId = getChildElementValue(depElement, "artifactId");
                dep.version = getChildElementValue(depElement, "version");
                dep.scope = getChildElementValue(depElement, "scope");
                deps.add(dep);
            }
        }
        
        return deps;
    }

    private List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<>();
        NodeList buildList = projectElement.getElementsByTagName("build");
        
        if (buildList.getLength() > 0) {
            Element buildElement = (Element) buildList.item(0);
            NodeList pluginsList = buildElement.getElementsByTagName("plugins");
            
            if (pluginsList.getLength() > 0) {
                Element pluginsElement = (Element) pluginsList.item(0);
                NodeList pluginNodes = pluginsElement.getElementsByTagName("plugin");
                
                for (int i = 0; i < pluginNodes.getLength(); i++) {
                    Element pluginElement = (Element) pluginNodes.item(i);
                    Plugin plugin = new Plugin();
                    plugin.groupId = getChildElementValue(pluginElement, "groupId");
                    plugin.artifactId = getChildElementValue(pluginElement, "artifactId");
                    plugin.version = getChildElementValue(pluginElement, "version");
                    
                    NodeList configList = pluginElement.getElementsByTagName("configuration");
                    if (configList.getLength() > 0) {
                        plugin.configurationElement = (Element) configList.item(0);
                    }
                    
                    plugins.add(plugin);
                }
            }
        }
        
        return plugins;
    }

    private boolean hasDependency(List<Dependency> dependencies, String groupId, String artifactId) {
        return findDependency(dependencies, groupId, artifactId) \!= null;
    }

    private Dependency findDependency(List<Dependency> dependencies, String groupId, String artifactId) {
        for (Dependency dep : dependencies) {
            if (groupId.equals(dep.groupId) && artifactId.equals(dep.artifactId)) {
                return dep;
            }
        }
        return null;
    }

    private Plugin findPlugin(List<Plugin> plugins, String groupId, String artifactId) {
        for (Plugin plugin : plugins) {
            if (groupId.equals(plugin.groupId) && artifactId.equals(plugin.artifactId)) {
                return plugin;
            }
        }
        return null;
    }

    // Helper classes
    private static class Dependency {
        String groupId;
        String artifactId;
        String version;
        String scope;
    }

    private static class Plugin {
        String groupId;
        String artifactId;
        String version;
        Element configurationElement;
    }
}