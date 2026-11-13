package JPA2.tests;

import JPA2.models.Address;
import JPA2.models.Employee;
import JPA2.models.EmployeeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.junit.Assert.*;

/**
 * Edge case tests for Address embeddable class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class AddressEdgeCaseTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testAddressAllNullFields() {
        Address address = new Address();
        address.setStreet(null);
        address.setCity(null);
        address.setState(null);
        address.setZip(null);

        assertNull(address.getStreet());
        assertNull(address.getCity());
        assertNull(address.getState());
        assertNull(address.getZip());
    }

    @Test
    public void testAddressAllEmptyFields() {
        Address address = new Address();
        address.setStreet("");
        address.setCity("");
        address.setState("");
        address.setZip("");

        assertEquals("", address.getStreet());
        assertEquals("", address.getCity());
        assertEquals("", address.getState());
        assertEquals("", address.getZip());
    }

    @Test
    public void testAddressWithVeryLongStreet() {
        StringBuilder longStreet = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longStreet.append("123 Main Street Apartment Building ");
        }

        Address address = new Address();
        address.setStreet(longStreet.toString());

        assertEquals(longStreet.toString(), address.getStreet());
    }

    @Test
    public void testAddressWithSpecialCharacters() {
        Address address = new Address();
        address.setStreet("123-A Main St. Apt #45");
        address.setCity("San José");
        address.setState("Cali$fornia");
        address.setZip("12345-6789");

        assertEquals("123-A Main St. Apt #45", address.getStreet());
        assertEquals("San José", address.getCity());
        assertEquals("Cali$fornia", address.getState());
        assertEquals("12345-6789", address.getZip());
    }

    @Test
    public void testAddressWithUnicodeCharacters() {
        Address address = new Address();
        address.setStreet("北京路123号");
        address.setCity("東京");
        address.setState("서울특별시");
        address.setZip("مدينة");

        assertEquals("北京路123号", address.getStreet());
        assertEquals("東京", address.getCity());
        assertEquals("서울특별시", address.getState());
        assertEquals("مدينة", address.getZip());
    }

    @Test
    @Rollback(false)
    public void testAddressNullFieldsInEmployee() {
        Employee emp = new Employee();
        emp.setName("Null Address Fields");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        Address address = new Address();
        address.setStreet(null);
        address.setCity(null);
        address.setState(null);
        address.setZip(null);
        emp.setAddress(address);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getAddress());
    }

    @Test
    @Rollback(false)
    public void testAddressPartiallyNullFields() {
        Employee emp = new Employee();
        emp.setName("Partial Address");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;

        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Springfield");
        address.setState(null);
        address.setZip(null);
        emp.setAddress(address);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found.getAddress());
        assertEquals("123 Main St", found.getAddress().getStreet());
        assertEquals("Springfield", found.getAddress().getCity());
    }

    @Test
    @Rollback(false)
    public void testAddressUpdate() {
        Employee emp = new Employee();
        emp.setName("Address Updater");
        emp.setSalary(60000);
        emp.type = EmployeeType.ENUM1;

        Address address = new Address();
        address.setStreet("Old Street");
        address.setCity("Old City");
        address.setState("Old State");
        address.setZip("00000");
        emp.setAddress(address);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        found.getAddress().setStreet("New Street");
        found.getAddress().setCity("New City");
        found.getAddress().setState("New State");
        found.getAddress().setZip("99999");
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals("New Street", updated.getAddress().getStreet());
        assertEquals("New City", updated.getAddress().getCity());
        assertEquals("New State", updated.getAddress().getState());
        assertEquals("99999", updated.getAddress().getZip());
    }

    @Test
    @Rollback(false)
    public void testAddressReplacement() {
        Employee emp = new Employee();
        emp.setName("Address Replacer");
        emp.setSalary(50000);
        emp.type = EmployeeType.TEST1;

        Address address1 = new Address();
        address1.setStreet("First Street");
        emp.setAddress(address1);

        em.persist(emp);
        em.flush();

        int empId = emp.getId();

        Employee found = em.find(Employee.class, empId);
        Address address2 = new Address();
        address2.setStreet("Second Street");
        address2.setCity("Second City");
        found.setAddress(address2);
        em.flush();

        Employee updated = em.find(Employee.class, empId);
        assertEquals("Second Street", updated.getAddress().getStreet());
        assertEquals("Second City", updated.getAddress().getCity());
    }

    @Test
    public void testAddressWithNumericStreet() {
        Address address = new Address();
        address.setStreet("12345");
        address.setCity("67890");
        address.setState("11111");
        address.setZip("22222");

        assertEquals("12345", address.getStreet());
        assertEquals("67890", address.getCity());
    }

    @Test
    public void testAddressMultipleInstances() {
        Address addr1 = new Address();
        addr1.setStreet("Street 1");

        Address addr2 = new Address();
        addr2.setStreet("Street 2");

        Address addr3 = new Address();
        addr3.setStreet("Street 3");

        assertNotEquals(addr1.getStreet(), addr2.getStreet());
        assertNotEquals(addr2.getStreet(), addr3.getStreet());
    }

    @Test
    public void testAddressVeryLongZipCode() {
        Address address = new Address();
        address.setZip("12345-67890-ABCDEF-GHIJKL");

        assertEquals("12345-67890-ABCDEF-GHIJKL", address.getZip());
    }
}
