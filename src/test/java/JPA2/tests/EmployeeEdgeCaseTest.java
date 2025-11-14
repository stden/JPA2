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
 * Edge case and boundary condition tests for Employee entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class EmployeeEdgeCaseTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testEmployeeWithNullName() {
        Employee emp = new Employee();
        emp.setName(null);
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNull(found.getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithEmptyName() {
        Employee emp = new Employee();
        emp.setName("");
        emp.setSalary(60000);
        emp.type = EmployeeType.TEST1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals("", found.getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithZeroSalary() {
        Employee emp = new Employee();
        emp.setName("Zero Salary Employee");
        emp.setSalary(0);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(0, found.getSalary());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithNegativeSalary() {
        Employee emp = new Employee();
        emp.setName("Negative Salary");
        emp.setSalary(-1000);
        emp.type = EmployeeType.TEST1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(-1000, found.getSalary());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithVeryLargeSalary() {
        Employee emp = new Employee();
        emp.setName("Wealthy Employee");
        emp.setSalary(Long.MAX_VALUE);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(Long.MAX_VALUE, found.getSalary());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithLongName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longName.append("VeryLongName");
        }

        Employee emp = new Employee();
        emp.setName(longName.toString());
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(longName.toString(), found.getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithSpecialCharactersInName() {
        Employee emp = new Employee();
        emp.setName("Test!@#$%^&*()_+-={}[]|\\:\";<>?,./");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals("Test!@#$%^&*()_+-={}[]|\\:\";<>?,./", found.getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithUnicodeCharacters() {
        Employee emp = new Employee();
        emp.setName("测试 テスト 테스트 مرحبا");
        emp.setSalary(60000);
        emp.type = EmployeeType.TEST1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals("测试 テスト 테스트 مرحبا", found.getName());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithEmptyPhoneNumbers() {
        Employee emp = new Employee();
        emp.setName("Empty Phones");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;
        emp.setPhoneNumbers(new HashMap<>());

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getPhoneNumbers());
        assertEquals(0, found.getPhoneNumbers().size());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithNullPhoneNumbers() {
        Employee emp = new Employee();
        emp.setName("Null Phones");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;
        emp.setPhoneNumbers(null);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithMultiplePhoneNumbers() {
        Employee emp = new Employee();
        emp.setName("Many Phones");
        emp.setSalary(60000);
        emp.type = EmployeeType.ENUM1;

        Map<String, String> phones = new HashMap<>();
        phones.put("Home", "555-1111");
        phones.put("Work", "555-2222");
        phones.put("Mobile", "555-3333");
        phones.put("Fax", "555-4444");
        phones.put("Emergency", "555-5555");
        emp.setPhoneNumbers(phones);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(5, found.getPhoneNumbers().size());
        assertEquals("555-1111", found.getPhoneNumbers().get("Home"));
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithNullAddress() {
        Employee emp = new Employee();
        emp.setName("No Address");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;
        emp.setAddress(null);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNull(found.getAddress());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithEmptyProjects() {
        Employee emp = new Employee();
        emp.setName("No Projects");
        emp.setSalary(55000);
        emp.type = EmployeeType.ENUM1;
        emp.setProjects(new ArrayList<>());

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getProjects());
        assertEquals(0, found.getProjects().size());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithNullDates() {
        Employee emp = new Employee();
        emp.setName("No Dates");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;
        emp.setDob(null);
        emp.setStartDate(null);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNull(found.getDob());
        assertNull(found.getStartDate());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithFutureDates() {
        Employee emp = new Employee();
        emp.setName("Future Dates");
        emp.setSalary(60000);
        emp.type = EmployeeType.ENUM1;

        Calendar futureDate = Calendar.getInstance();
        futureDate.set(2099, Calendar.DECEMBER, 31);
        emp.setDob(futureDate);

        Date futureStartDate = new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000);
        emp.setStartDate(futureStartDate);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getDob());
        assertNotNull(found.getStartDate());
    }

    @Test
    @Rollback(false)
    public void testEmployeeWithVeryOldDates() {
        Employee emp = new Employee();
        emp.setName("Old Dates");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;

        Calendar oldDate = Calendar.getInstance();
        oldDate.set(1900, Calendar.JANUARY, 1);
        emp.setDob(oldDate);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getDob());
    }

    @Test
    @Rollback(false)
    public void testEmployeeMultiplePersistFlush() {
        Employee emp = new Employee();
        emp.setName("Multiple Flush");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        int id1 = emp.getId();

        emp.setName("Updated Name");
        em.flush();

        emp.setSalary(60000);
        em.flush();

        Employee found = em.find(Employee.class, id1);
        assertNotNull(found);
        assertEquals("Updated Name", found.getName());
        assertEquals(60000, found.getSalary());
    }

    @Test
    @Rollback(false)
    public void testEmployeeUpdateAllFields() {
        Employee emp = new Employee();
        emp.setName("Original");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;
        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.setName("Updated");
        found.setSalary(70000);
        found.setType(EmployeeType.ENUM1);

        Calendar dob = Calendar.getInstance();
        dob.set(1995, Calendar.MAY, 15);
        found.setDob(dob);

        found.setStartDate(new Date());

        Address address = new Address();
        address.setCity("New City");
        found.setAddress(address);

        Map<String, String> phones = new HashMap<>();
        phones.put("New", "555-9999");
        found.setPhoneNumbers(phones);

        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals("Updated", updated.getName());
        assertEquals(70000, updated.getSalary());
        assertEquals(EmployeeType.ENUM1, updated.getType());
        assertNotNull(updated.getDob());
        assertNotNull(updated.getStartDate());
        assertNotNull(updated.getAddress());
        assertEquals("New City", updated.getAddress().getCity());
        assertEquals(1, updated.getPhoneNumbers().size());
    }

    @Test
    public void testEmployeeEquality() {
        Employee emp1 = new Employee();
        emp1.setId(100);
        emp1.setName("Test");

        Employee emp2 = new Employee();
        emp2.setId(100);
        emp2.setName("Test");

        assertEquals(emp1.getId(), emp2.getId());
        assertEquals(emp1.getName(), emp2.getName());
    }
}
