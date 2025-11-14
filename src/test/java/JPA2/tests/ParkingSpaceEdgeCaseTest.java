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
 * Edge case tests for ParkingSpace entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class ParkingSpaceEdgeCaseTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testParkingSpaceWithNullLocation() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9000);
        space.setLot(1);
        space.setLocation(null);

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9000);
        assertNotNull(found);
        assertNull(found.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithEmptyLocation() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9001);
        space.setLot(2);
        space.setLocation("");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9001);
        assertNotNull(found);
        assertEquals("", found.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithVeryLongLocation() {
        StringBuilder longLocation = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longLocation.append("Building X Floor Y Section Z ");
        }

        ParkingSpace space = new ParkingSpace();
        space.setId(9002);
        space.setLot(3);
        space.setLocation(longLocation.toString());

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9002);
        assertNotNull(found);
        assertEquals(longLocation.toString(), found.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithSpecialCharacters() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9003);
        space.setLot(4);
        space.setLocation("Bldg A-1, Level #2, Section (North)");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9003);
        assertNotNull(found);
        assertEquals("Bldg A-1, Level #2, Section (North)", found.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithZeroLot() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9004);
        space.setLot(0);
        space.setLocation("Ground Level");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9004);
        assertNotNull(found);
        assertEquals(0, found.getLot());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithNegativeLot() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9005);
        space.setLot(-1);
        space.setLocation("Basement Level 1");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9005);
        assertNotNull(found);
        assertEquals(-1, found.getLot());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithVeryHighLot() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9006);
        space.setLot(Integer.MAX_VALUE);
        space.setLocation("Top Floor");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9006);
        assertNotNull(found);
        assertEquals(Integer.MAX_VALUE, found.getLot());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceUpdateLot() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9007);
        space.setLot(1);
        space.setLocation("Original");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9007);
        found.setLot(99);
        em.flush();

        ParkingSpace updated = em.find(ParkingSpace.class, 9007);
        assertEquals(99, updated.getLot());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceUpdateLocation() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9008);
        space.setLot(5);
        space.setLocation("Old Location");

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9008);
        found.setLocation("New Location");
        em.flush();

        ParkingSpace updated = em.find(ParkingSpace.class, 9008);
        assertEquals("New Location", updated.getLocation());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceWithNullEmployee() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9009);
        space.setLot(6);
        space.setLocation("Vacant");
        space.setEmployee(null);

        em.persist(space);
        em.flush();

        ParkingSpace found = em.find(ParkingSpace.class, 9009);
        assertNotNull(found);
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceReassignment() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9010);
        space.setLot(7);
        space.setLocation("Reassignable");
        em.persist(space);

        Employee emp1 = new Employee();
        emp1.setName("First Owner");
        emp1.setSalary(50000);
        emp1.type = EmployeeType.ENUM1;
        emp1.setParkingSpace(space);
        em.persist(emp1);
        em.flush();

        Employee emp2 = new Employee();
        emp2.setName("Second Owner");
        emp2.setSalary(55000);
        emp2.type = EmployeeType.TEST1;
        em.persist(emp2);
        em.flush();

        // Reassign parking space
        Employee found1 = em.find(Employee.class, emp1.getId());
        found1.setParkingSpace(null);

        Employee found2 = em.find(Employee.class, emp2.getId());
        found2.setParkingSpace(space);
        em.flush();

        Employee updated2 = em.find(Employee.class, emp2.getId());
        assertNotNull(updated2.getParkingSpace());
        assertEquals(7, updated2.getParkingSpace().getLot());
    }

    @Test
    @Rollback(false)
    public void testParkingSpaceMultipleUpdates() {
        ParkingSpace space = new ParkingSpace();
        space.setId(9011);
        space.setLot(1);
        space.setLocation("Initial");

        em.persist(space);
        em.flush();

        for (int i = 0; i < 10; i++) {
            ParkingSpace found = em.find(ParkingSpace.class, 9011);
            found.setLot(i + 1);
            found.setLocation("Update " + i);
            em.flush();
        }

        ParkingSpace final1 = em.find(ParkingSpace.class, 9011);
        assertEquals(10, final1.getLot());
        assertEquals("Update 9", final1.getLocation());
    }
}
