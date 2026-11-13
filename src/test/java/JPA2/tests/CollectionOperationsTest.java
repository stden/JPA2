package JPA2.tests;

import JPA2.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for collection operations on entity relationships
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class CollectionOperationsTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testAddProjectsIncrementally() {
        Employee emp = new Employee();
        emp.setName("Incremental Projects");
        emp.setSalary(60000);
        emp.type = EmployeeType.ENUM1;
        emp.setProjects(new ArrayList<>());
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        for (int i = 0; i < 5; i++) {
            Project project = new Project();
            project.setId(6000 + i);
            project.setName("Project " + i);
            em.persist(project);

            Employee found = em.find(Employee.class, empId);
            Collection<Project> projects = new ArrayList<>(found.getProjects());
            projects.add(project);
            found.setProjects(projects);
            em.flush();
        }

        Employee final1 = em.find(Employee.class, empId);
        assertEquals(5, final1.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testReplaceAllProjects() {
        Project p1 = new Project();
        p1.setId(6100);
        p1.setName("Old Project 1");
        em.persist(p1);

        Project p2 = new Project();
        p2.setId(6101);
        p2.setName("Old Project 2");
        em.persist(p2);

        Employee emp = new Employee();
        emp.setName("Project Replacer");
        emp.setSalary(65000);
        emp.type = EmployeeType.TEST1;

        Collection<Project> oldProjects = new ArrayList<>();
        oldProjects.add(p1);
        oldProjects.add(p2);
        emp.setProjects(oldProjects);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        // Replace all projects
        Project p3 = new Project();
        p3.setId(6102);
        p3.setName("New Project 1");
        em.persist(p3);

        Project p4 = new Project();
        p4.setId(6103);
        p4.setName("New Project 2");
        em.persist(p4);

        Employee found = em.find(Employee.class, empId);
        Collection<Project> newProjects = new ArrayList<>();
        newProjects.add(p3);
        newProjects.add(p4);
        found.setProjects(newProjects);
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals(2, updated.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testClearAllProjects() {
        Project p1 = new Project();
        p1.setId(6200);
        p1.setName("Temp Project");
        em.persist(p1);

        Employee emp = new Employee();
        emp.setName("Project Clearer");
        emp.setSalary(55000);
        emp.type = EmployeeType.ENUM1;

        Collection<Project> projects = new ArrayList<>();
        projects.add(p1);
        emp.setProjects(projects);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.setProjects(new ArrayList<>());
        em.flush();

        Employee cleared = em.find(Employee.class, empId);
        assertNotNull(cleared.getProjects());
        assertEquals(0, cleared.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testAddPhoneNumbersIncrementally() {
        Employee emp = new Employee();
        emp.setName("Phone Collector");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;
        emp.setPhoneNumbers(new HashMap<>());
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        String[] phoneTypes = {"Home", "Work", "Mobile", "Fax", "Emergency", "Personal"};
        for (int i = 0; i < phoneTypes.length; i++) {
            Employee found = em.find(Employee.class, empId);
            found.getPhoneNumbers().put(phoneTypes[i], "555-" + (1000 + i));
            em.flush();
        }

        Employee final1 = em.find(Employee.class, empId);
        assertEquals(6, final1.getPhoneNumbers().size());
    }

    @Test
    @Rollback(false)
    public void testUpdatePhoneNumber() {
        Employee emp = new Employee();
        emp.setName("Phone Updater");
        emp.setSalary(55000);
        emp.type = EmployeeType.ENUM1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Work", "555-0000");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.getPhoneNumbers().put("Work", "555-9999");
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals("555-9999", updated.getPhoneNumbers().get("Work"));
    }

    @Test
    @Rollback(false)
    public void testRemoveSpecificPhoneNumber() {
        Employee emp = new Employee();
        emp.setName("Phone Remover");
        emp.setSalary(60000);
        emp.type = EmployeeType.TEST1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Home", "555-1111");
        phones.put("Work", "555-2222");
        phones.put("Mobile", "555-3333");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.getPhoneNumbers().remove("Work");
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals(2, updated.getPhoneNumbers().size());
        assertFalse(updated.getPhoneNumbers().containsKey("Work"));
        assertTrue(updated.getPhoneNumbers().containsKey("Home"));
        assertTrue(updated.getPhoneNumbers().containsKey("Mobile"));
    }

    @Test
    @Rollback(false)
    public void testClearAllPhoneNumbers() {
        Employee emp = new Employee();
        emp.setName("Phone Clearer");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Home", "555-1111");
        phones.put("Work", "555-2222");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.getPhoneNumbers().clear();
        em.flush();

        Employee cleared = em.find(Employee.class, empId);
        assertNotNull(cleared.getPhoneNumbers());
        assertEquals(0, cleared.getPhoneNumbers().size());
    }

    @Test
    @Rollback(false)
    public void testReplacePhoneNumbersMap() {
        Employee emp = new Employee();
        emp.setName("Phone Replacer");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;

        Map<String, String> oldPhones = new HashMap<>();
        oldPhones.put("Old1", "555-1111");
        emp.setPhoneNumbers(oldPhones);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        Map<String, String> newPhones = new HashMap<>();
        newPhones.put("New1", "555-9999");
        newPhones.put("New2", "555-8888");
        found.setPhoneNumbers(newPhones);
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals(2, updated.getPhoneNumbers().size());
        assertTrue(updated.getPhoneNumbers().containsKey("New1"));
        assertFalse(updated.getPhoneNumbers().containsKey("Old1"));
    }

    @Test
    @Rollback(false)
    public void testPhoneNumbersWithSpecialCharacters() {
        Employee emp = new Employee();
        emp.setName("Special Phones");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Primary (Mobile)", "+1-555-1234");
        phones.put("Office/Direct", "555-5678 ext. 123");
        phones.put("International", "+44-20-1234-5678");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertEquals(3, found.getPhoneNumbers().size());
        assertEquals("+1-555-1234", found.getPhoneNumbers().get("Primary (Mobile)"));
    }

    @Test
    @Rollback(false)
    public void testLargeProjectCollection() {
        Employee emp = new Employee();
        emp.setName("Many Projects");
        emp.setSalary(70000);
        emp.type = EmployeeType.TEST1;

        Collection<Project> projects = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Project project = new Project();
            project.setId(6300 + i);
            project.setName("Mass Project " + i);
            em.persist(project);
            projects.add(project);
        }
        emp.setProjects(projects);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertEquals(20, found.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testLargePhoneNumbersMap() {
        Employee emp = new Employee();
        emp.setName("Many Phones");
        emp.setSalary(60000);
        emp.type = EmployeeType.ENUM1;

        Map<String, String> phones = new HashMap<>();
        for (int i = 0; i < 15; i++) {
            phones.put("Phone" + i, "555-" + (2000 + i));
        }
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertEquals(15, found.getPhoneNumbers().size());
    }

    @Test
    @Rollback(false)
    public void testProjectCollectionWithDuplicates() {
        Project project = new Project();
        project.setId(6400);
        project.setName("Duplicate Test");
        em.persist(project);

        Employee emp = new Employee();
        emp.setName("Duplicate Projects");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;

        Collection<Project> projects = new ArrayList<>();
        projects.add(project);
        projects.add(project); // Adding same project twice
        emp.setProjects(projects);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found.getProjects());
    }

    @Test
    @Rollback(false)
    public void testPhoneMapIterationAndUpdate() {
        Employee emp = new Employee();
        emp.setName("Phone Iterator");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Home", "555-1111");
        phones.put("Work", "555-2222");
        phones.put("Mobile", "555-3333");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        Map<String, String> updatedPhones = new HashMap<>();
        for (Map.Entry<String, String> entry : found.getPhoneNumbers().entrySet()) {
            updatedPhones.put(entry.getKey(), entry.getValue().replace("555", "777"));
        }
        found.setPhoneNumbers(updatedPhones);
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertTrue(updated.getPhoneNumbers().get("Home").contains("777"));
    }

    @Test
    @Rollback(false)
    public void testProjectCollectionToEmptyAndBack() {
        Project project = new Project();
        project.setId(6500);
        project.setName("Toggle Project");
        em.persist(project);

        Employee emp = new Employee();
        emp.setName("Project Toggler");
        emp.setSalary(60000);
        emp.type = EmployeeType.TEST1;

        Collection<Project> projects = new ArrayList<>();
        projects.add(project);
        emp.setProjects(projects);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        // Empty it
        Employee found = em.find(Employee.class, empId);
        found.setProjects(new ArrayList<>());
        em.flush();

        Employee empty = em.find(Employee.class, empId);
        assertEquals(0, empty.getProjects().size());

        // Add it back
        Collection<Project> newProjects = new ArrayList<>();
        newProjects.add(project);
        empty.setProjects(newProjects);
        em.flush();

        Employee restored = em.find(Employee.class, empId);
        assertEquals(1, restored.getProjects().size());
    }
}
