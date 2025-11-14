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
 * Integration tests testing multiple entities working together
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class EntityIntegrationTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testCompleteOrganizationStructure() {
        // Create department
        Department dept = new Department();
        dept.setId(5000);
        dept.setName("Engineering");
        em.persist(dept);

        // Create projects
        Project project1 = new Project();
        project1.setId(5001);
        project1.setName("Web Platform");
        em.persist(project1);

        Project project2 = new Project();
        project2.setId(5002);
        project2.setName("Mobile App");
        em.persist(project2);

        // Create parking spaces
        ParkingSpace parking1 = new ParkingSpace();
        parking1.setId(5010);
        parking1.setLot(1);
        parking1.setLocation("Building A");
        em.persist(parking1);

        ParkingSpace parking2 = new ParkingSpace();
        parking2.setId(5011);
        parking2.setLot(2);
        parking2.setLocation("Building B");
        em.persist(parking2);

        // Create employees
        Employee emp1 = new Employee();
        emp1.setName("Senior Developer");
        emp1.setSalary(90000);
        emp1.type = EmployeeType.ENUM1;
        emp1.setDepartment(dept);
        emp1.setParkingSpace(parking1);

        Collection<Project> projects1 = new ArrayList<>();
        projects1.add(project1);
        projects1.add(project2);
        emp1.setProjects(projects1);

        Address addr1 = new Address();
        addr1.setStreet("123 Main St");
        addr1.setCity("Tech City");
        addr1.setState("CA");
        addr1.setZip("12345");
        emp1.setAddress(addr1);

        Map<String, String> phones1 = new HashMap<>();
        phones1.put("Work", "555-1000");
        phones1.put("Mobile", "555-1001");
        emp1.setPhoneNumbers(phones1);

        em.persist(emp1);

        Employee emp2 = new Employee();
        emp2.setName("Junior Developer");
        emp2.setSalary(60000);
        emp2.type = EmployeeType.TEST1;
        emp2.setDepartment(dept);
        emp2.setParkingSpace(parking2);

        Collection<Project> projects2 = new ArrayList<>();
        projects2.add(project1);
        emp2.setProjects(projects2);

        em.persist(emp2);

        em.flush();

        // Verify complete structure
        Employee foundEmp1 = em.find(Employee.class, emp1.getId());
        assertNotNull(foundEmp1);
        assertEquals("Senior Developer", foundEmp1.getName());
        assertNotNull(foundEmp1.getDepartment());
        assertEquals("Engineering", foundEmp1.getDepartment().getName());
        assertNotNull(foundEmp1.getParkingSpace());
        assertEquals(1, foundEmp1.getParkingSpace().getLot());
        assertNotNull(foundEmp1.getProjects());
        assertEquals(2, foundEmp1.getProjects().size());
        assertNotNull(foundEmp1.getAddress());
        assertEquals("Tech City", foundEmp1.getAddress().getCity());
        assertEquals(2, foundEmp1.getPhoneNumbers().size());
    }

    @Test
    @Rollback(false)
    public void testMultipleEmployeesOneDepartment() {
        Department dept = new Department();
        dept.setId(5100);
        dept.setName("Sales");
        em.persist(dept);

        for (int i = 0; i < 10; i++) {
            Employee emp = new Employee();
            emp.setName("Sales Person " + i);
            emp.setSalary(45000 + i * 1000);
            emp.type = i % 2 == 0 ? EmployeeType.ENUM1 : EmployeeType.TEST1;
            emp.setDepartment(dept);
            em.persist(emp);
        }

        em.flush();

        Department found = em.find(Department.class, 5100);
        assertNotNull(found);
        assertEquals("Sales", found.getName());
    }

    @Test
    @Rollback(false)
    public void testMultipleEmployeesOneProject() {
        Project project = new Project();
        project.setId(5200);
        project.setName("Big Project");
        em.persist(project);

        for (int i = 0; i < 5; i++) {
            Employee emp = new Employee();
            emp.setName("Team Member " + i);
            emp.setSalary(55000);
            emp.type = EmployeeType.ENUM1;

            Collection<Project> projects = new ArrayList<>();
            projects.add(project);
            emp.setProjects(projects);

            em.persist(emp);
        }

        em.flush();

        Project found = em.find(Project.class, 5200);
        assertNotNull(found);
        assertEquals("Big Project", found.getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeChangeDepartment() {
        Department dept1 = new Department();
        dept1.setId(5300);
        dept1.setName("HR");
        em.persist(dept1);

        Department dept2 = new Department();
        dept2.setId(5301);
        dept2.setName("Finance");
        em.persist(dept2);

        Employee emp = new Employee();
        emp.setName("Transferred Employee");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;
        emp.setDepartment(dept1);
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        assertEquals("HR", found.getDepartment().getName());

        found.setDepartment(dept2);
        em.flush();

        Employee transferred = em.find(Employee.class, empId);
        assertEquals("Finance", transferred.getDepartment().getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeChangeParkingSpace() {
        ParkingSpace space1 = new ParkingSpace();
        space1.setId(5400);
        space1.setLot(1);
        space1.setLocation("Level 1");
        em.persist(space1);

        ParkingSpace space2 = new ParkingSpace();
        space2.setId(5401);
        space2.setLot(2);
        space2.setLocation("Level 2");
        em.persist(space2);

        Employee emp = new Employee();
        emp.setName("Parker");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;
        emp.setParkingSpace(space1);
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        assertEquals(1, found.getParkingSpace().getLot());

        found.setParkingSpace(space2);
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals(2, updated.getParkingSpace().getLot());
    }

    @Test
    @Rollback(false)
    public void testEmployeeAddRemoveProjects() {
        Project project1 = new Project();
        project1.setId(5500);
        project1.setName("Project A");
        em.persist(project1);

        Project project2 = new Project();
        project2.setId(5501);
        project2.setName("Project B");
        em.persist(project2);

        Project project3 = new Project();
        project3.setId(5502);
        project3.setName("Project C");
        em.persist(project3);

        Employee emp = new Employee();
        emp.setName("Multi-Project Worker");
        emp.setSalary(70000);
        emp.type = EmployeeType.ENUM1;

        Collection<Project> projects = new ArrayList<>();
        projects.add(project1);
        emp.setProjects(projects);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        assertEquals(1, found.getProjects().size());

        // Add more projects
        Collection<Project> updatedProjects = new ArrayList<>(found.getProjects());
        updatedProjects.add(project2);
        updatedProjects.add(project3);
        found.setProjects(updatedProjects);
        em.flush();

        Employee withMoreProjects = em.find(Employee.class, empId);
        assertEquals(3, withMoreProjects.getProjects().size());

        // Remove a project
        Collection<Project> reducedProjects = new ArrayList<>();
        reducedProjects.add(project1);
        withMoreProjects.setProjects(reducedProjects);
        em.flush();

        Employee withFewerProjects = em.find(Employee.class, empId);
        assertEquals(1, withFewerProjects.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testEmployeeUpdatePhoneNumbers() {
        Employee emp = new Employee();
        emp.setName("Phone Updater");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Home", "555-0001");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        assertEquals(1, found.getPhoneNumbers().size());

        // Add more numbers
        found.getPhoneNumbers().put("Work", "555-0002");
        found.getPhoneNumbers().put("Mobile", "555-0003");
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals(3, updated.getPhoneNumbers().size());

        // Remove a number
        updated.getPhoneNumbers().remove("Home");
        em.flush();

        Employee final1 = em.find(Employee.class, empId);
        assertEquals(2, final1.getPhoneNumbers().size());
        assertFalse(final1.getPhoneNumbers().containsKey("Home"));
    }

    @Test
    @Rollback(false)
    public void testEmployeeRemoveAndReassignDepartment() {
        Department dept = new Department();
        dept.setId(5600);
        dept.setName("Temporary Dept");
        em.persist(dept);

        Employee emp = new Employee();
        emp.setName("Reassignable");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;
        emp.setDepartment(dept);
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.setDepartment(null);
        em.flush();

        Employee withoutDept = em.find(Employee.class, empId);
        assertNull(withoutDept.getDepartment());

        Department newDept = new Department();
        newDept.setId(5601);
        newDept.setName("New Department");
        em.persist(newDept);

        withoutDept.setDepartment(newDept);
        em.flush();

        Employee reassigned = em.find(Employee.class, empId);
        assertNotNull(reassigned.getDepartment());
        assertEquals("New Department", reassigned.getDepartment().getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeRemoveParkingSpace() {
        ParkingSpace space = new ParkingSpace();
        space.setId(5700);
        space.setLot(5);
        space.setLocation("Temp");
        em.persist(space);

        Employee emp = new Employee();
        emp.setName("Temporary Parker");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;
        emp.setParkingSpace(space);
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        assertNotNull(found.getParkingSpace());

        found.setParkingSpace(null);
        em.flush();

        Employee withoutParking = em.find(Employee.class, empId);
        assertNull(withoutParking.getParkingSpace());
    }

    @Test
    @Rollback(false)
    public void testComplexUpdate() {
        // Create initial structure
        Department dept1 = new Department();
        dept1.setId(5800);
        dept1.setName("Dept 1");
        em.persist(dept1);

        ParkingSpace space1 = new ParkingSpace();
        space1.setId(5801);
        space1.setLot(1);
        space1.setLocation("Space 1");
        em.persist(space1);

        Employee emp = new Employee();
        emp.setName("Complex Employee");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;
        emp.setDepartment(dept1);
        emp.setParkingSpace(space1);
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        // Update everything
        Department dept2 = new Department();
        dept2.setId(5802);
        dept2.setName("Dept 2");
        em.persist(dept2);

        ParkingSpace space2 = new ParkingSpace();
        space2.setId(5803);
        space2.setLot(2);
        space2.setLocation("Space 2");
        em.persist(space2);

        Employee found = em.find(Employee.class, empId);
        found.setName("Updated Name");
        found.setSalary(80000);
        found.setType(EmployeeType.TEST1);
        found.setDepartment(dept2);
        found.setParkingSpace(space2);

        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals("Updated Name", updated.getName());
        assertEquals(80000, updated.getSalary());
        assertEquals(EmployeeType.TEST1, updated.getType());
        assertEquals("Dept 2", updated.getDepartment().getName());
        assertEquals(2, updated.getParkingSpace().getLot());
    }

    @Test
    @Rollback(false)
    public void testMultipleFlushOperations() {
        Employee emp = new Employee();
        emp.setName("Multi Flush");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        for (int i = 0; i < 5; i++) {
            Employee found = em.find(Employee.class, empId);
            found.setSalary(found.getSalary() + 1000);
            em.flush();
        }

        Employee final1 = em.find(Employee.class, empId);
        assertEquals(55000, final1.getSalary());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithAllNullRelationships() {
        Employee emp = new Employee();
        emp.setName("No Relationships");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;
        emp.setDepartment(null);
        emp.setParkingSpace(null);
        emp.setProjects(null);
        emp.setAddress(null);
        emp.setPhoneNumbers(null);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNull(found.getDepartment());
        assertNull(found.getParkingSpace());
        assertNull(found.getAddress());
    }
}
