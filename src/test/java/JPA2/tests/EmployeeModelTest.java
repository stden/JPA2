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
 * Comprehensive tests for Employee entity covering all fields and relationships
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class EmployeeModelTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testEmployeeBasicFields() {
        Employee emp = new Employee();
        emp.setName("Test Employee");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals("Test Employee", found.getName());
        assertEquals(50000, found.getSalary());
        assertEquals(EmployeeType.ENUM1, found.type);
    }

    @Test
    @Rollback(false)
    public void testEmployeeConstructorWithId() {
        Employee emp = new Employee(999);
        assertEquals(999, emp.getId());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithDepartment() {
        Department dept = new Department();
        dept.setId(100);
        dept.setName("Engineering");
        em.persist(dept);

        Employee emp = new Employee();
        emp.setName("John Doe");
        emp.setSalary(60000);
        emp.type = EmployeeType.TEST1;
        emp.setDepartment(dept);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getDepartment());
        assertEquals("Engineering", found.getDepartment().getName());
        assertEquals(100, found.getDepartment().getId());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithParkingSpace() {
        ParkingSpace parking = new ParkingSpace();
        parking.setId(50);
        parking.setLot(1);
        parking.setLocation("Building A");
        em.persist(parking);

        Employee emp = new Employee();
        emp.setName("Jane Smith");
        emp.setSalary(55000);
        emp.type = EmployeeType.ENUM1;
        emp.setParkingSpace(parking);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getParkingSpace());
        assertEquals(1, found.getParkingSpace().getLot());
        assertEquals("Building A", found.getParkingSpace().getLocation());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithProjects() {
        Project project1 = new Project();
        project1.setId(200);
        project1.setName("Project Alpha");
        em.persist(project1);

        Project project2 = new Project();
        project2.setId(201);
        project2.setName("Project Beta");
        em.persist(project2);

        Employee emp = new Employee();
        emp.setName("Bob Wilson");
        emp.setSalary(65000);
        emp.type = EmployeeType.TEST1;

        Collection<Project> projects = new ArrayList<>();
        projects.add(project1);
        projects.add(project2);
        emp.setProjects(projects);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getProjects());
        assertEquals(2, found.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithAddress() {
        Employee emp = new Employee();
        emp.setName("Alice Brown");
        emp.setSalary(58000);
        emp.type = EmployeeType.ENUM1;

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("New York");
        address.setState("NY");
        address.setZip("10001");
        emp.setAddress(address);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getAddress());
        assertEquals("123 Main St", found.getAddress().getStreet());
        assertEquals("New York", found.getAddress().getCity());
        assertEquals("NY", found.getAddress().getState());
        assertEquals("10001", found.getAddress().getZip());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithPhoneNumbers() {
        Employee emp = new Employee();
        emp.setName("Charlie Davis");
        emp.setSalary(52000);
        emp.type = EmployeeType.TEST1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Home", "555-1234");
        phones.put("Mobile", "555-5678");
        phones.put("Work", "555-9012");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getPhoneNumbers());
        assertEquals(3, found.getPhoneNumbers().size());
        assertEquals("555-1234", found.getPhoneNumbers().get("Home"));
        assertEquals("555-5678", found.getPhoneNumbers().get("Mobile"));
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithDates() {
        Employee emp = new Employee();
        emp.setName("David Lee");
        emp.setSalary(70000);
        emp.type = EmployeeType.ENUM1;

        Calendar dob = Calendar.getInstance();
        dob.set(1990, Calendar.JANUARY, 15);
        emp.setDob(dob);

        Date startDate = new Date();
        emp.setStartDate(startDate);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getDob());
        assertNotNull(found.getStartDate());
    }

    @Test
    @Rollback(false)
    public void testEmployeeAllFields() {
        // Create all related entities
        Department dept = new Department();
        dept.setId(300);
        dept.setName("Sales");
        em.persist(dept);

        ParkingSpace parking = new ParkingSpace();
        parking.setId(301);
        parking.setLot(2);
        parking.setLocation("Building B");
        em.persist(parking);

        Project project = new Project();
        project.setId(302);
        project.setName("Complete Project");
        em.persist(project);

        // Create employee with all fields
        Employee emp = new Employee();
        emp.setName("Complete Employee");
        emp.setSalary(80000);
        emp.type = EmployeeType.ENUM1;

        Calendar dob = Calendar.getInstance();
        dob.set(1985, Calendar.MARCH, 20);
        emp.setDob(dob);

        emp.setStartDate(new Date());
        emp.setDepartment(dept);
        emp.setParkingSpace(parking);

        Collection<Project> projects = new ArrayList<>();
        projects.add(project);
        emp.setProjects(projects);

        Address address = new Address();
        address.setStreet("456 Oak Ave");
        address.setCity("Boston");
        address.setState("MA");
        address.setZip("02101");
        emp.setAddress(address);

        Map<String, String> phones = new HashMap<>();
        phones.put("Office", "555-0000");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        // Verify all fields
        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals("Complete Employee", found.getName());
        assertEquals(80000, found.getSalary());
        assertEquals(EmployeeType.ENUM1, found.type);
        assertNotNull(found.getDob());
        assertNotNull(found.getStartDate());
        assertNotNull(found.getDepartment());
        assertEquals("Sales", found.getDepartment().getName());
        assertNotNull(found.getParkingSpace());
        assertEquals(2, found.getParkingSpace().getLot());
        assertNotNull(found.getProjects());
        assertEquals(1, found.getProjects().size());
        assertNotNull(found.getAddress());
        assertEquals("456 Oak Ave", found.getAddress().getStreet());
        assertNotNull(found.getPhoneNumbers());
        assertEquals("555-0000", found.getPhoneNumbers().get("Office"));
    }

    @Test
    @Rollback(false)
    public void testEmployeeSetId() {
        Employee emp = new Employee();
        emp.setId(12345);
        assertEquals(12345, emp.getId());
    }
}
