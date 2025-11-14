package JPA2.tests;

import JPA2.models.Employee;
import JPA2.models.EmployeeType;
import JPA2.models.ParkingSpace;
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
 * Comprehensive tests for ParkingSpace entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class ParkingSpaceTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testParkingSpaceBasicFields() {
        ParkingSpace parking = new ParkingSpace();
        parking.setId(2000);
        parking.setLot(5);
        parking.setLocation("North Garage");

        em.persist(parking);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 2000);
        assertNotNull(found);
        assertEquals(2000, found.getId());
        assertEquals(5, found.getLot());
        assertEquals("North Garage", found.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithEmployee() {
        ParkingSpace parking = new ParkingSpace();
        parking.setId(2001);
        parking.setLot(3);
        parking.setLocation("South Garage");
        em.persist(parking);

        Employee emp = new Employee();
        emp.setName("John Parker");
        emp.setSalary(55000);
        emp.type = EmployeeType.ENUM1;
        emp.setParkingSpace(parking);
        em.persist(emp);

        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 2001);
        assertNotNull(found);
        assertEquals(3, found.getLot());
        assertEquals("South Garage", found.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceGettersSetters() {
        ParkingSpace parking = new ParkingSpace();
        parking.setId(2002);
        parking.setLot(10);
        parking.setLocation("West Garage");

        Employee emp = new Employee();
        parking.setEmployee(emp);

        assertEquals(2002, parking.getId());
        assertEquals(10, parking.getLot());
        assertEquals("West Garage", parking.getLocation());
        assertNotNull(parking.getEmployee());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceLotUpdate() {
        ParkingSpace parking = new ParkingSpace();
        parking.setId(2003);
        parking.setLot(1);
        parking.setLocation("East Garage");
        em.persist(parking);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 2003);
        found.setLot(2);
        found.setLocation("East Garage Level 2");
        em.flush();

        ParkingSpace updated = em.find(ParkingSpace.class, 2003);
        assertEquals(2, updated.getLot());
        assertEquals("East Garage Level 2", updated.getLocation());
    }
}
