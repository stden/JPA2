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
 * Comprehensive tests for Address embeddable class
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class AddressTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testAddressDefaultConstructor() {
        Address address = new Address();

        // Test default values set in constructor
        assertEquals("Санкт-Петербург", address.getCity());
        assertEquals("Северный проспект", address.getStreet());
        assertEquals("СПб и ЛО", address.getState());
        assertEquals("194295", address.getZip());
    }

    @Test
    public void testAddressGettersSetters() {
        Address address = new Address();

        address.setStreet("456 Elm Street");
        address.setCity("Moscow");
        address.setState("Moscow Oblast");
        address.setZip("101000");

        assertEquals("456 Elm Street", address.getStreet());
        assertEquals("Moscow", address.getCity());
        assertEquals("Moscow Oblast", address.getState());
        assertEquals("101000", address.getZip());
    }

    @Test
    @Rollback(false)
    public void testAddressEmbeddedInEmployee() {
        Employee emp = new Employee();
        emp.setName("Test Employee");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        Address address = new Address();
        address.setStreet("789 Oak Avenue");
        address.setCity("Saint Petersburg");
        address.setState("Leningrad Oblast");
        address.setZip("190000");
        emp.setAddress(address);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertNotNull(found.getAddress());
        assertEquals("789 Oak Avenue", found.getAddress().getStreet());
        assertEquals("Saint Petersburg", found.getAddress().getCity());
        assertEquals("Leningrad Oblast", found.getAddress().getState());
        assertEquals("190000", found.getAddress().getZip());
    }

    @Test
    public void testAddressAllFieldsModification() {
        Address address = new Address();

        // Verify initial defaults
        assertNotNull(address.getCity());
        assertNotNull(address.getStreet());
        assertNotNull(address.getState());
        assertNotNull(address.getZip());

        // Modify all fields
        address.setStreet("New Street");
        address.setCity("New City");
        address.setState("New State");
        address.setZip("99999");

        // Verify modifications
        assertEquals("New Street", address.getStreet());
        assertEquals("New City", address.getCity());
        assertEquals("New State", address.getState());
        assertEquals("99999", address.getZip());
    }

    @Test
    @Rollback(false)
    public void testAddressUpdateInEmployee() {
        Employee emp = new Employee();
        emp.setName("Update Test");
        emp.setSalary(60000);
        emp.type = EmployeeType.TEST1;

        Address address = new Address();
        emp.setAddress(address);
        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        Address foundAddress = found.getAddress();
        foundAddress.setStreet("Updated Street");
        foundAddress.setCity("Updated City");
        em.flush();

        Employee updated = em.find(Employee.class, emp.getId());
        assertEquals("Updated Street", updated.getAddress().getStreet());
        assertEquals("Updated City", updated.getAddress().getCity());
    }
}
