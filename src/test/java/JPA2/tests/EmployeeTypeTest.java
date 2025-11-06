package JPA2.tests;

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
 * Comprehensive tests for EmployeeType enum
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class EmployeeTypeTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEmployeeTypeValues() {
        EmployeeType[] types = EmployeeType.values();

        assertEquals(2, types.length);
        assertEquals(EmployeeType.TEST1, types[0]);
        assertEquals(EmployeeType.ENUM1, types[1]);
    }

    @Test
    public void testEmployeeTypeValueOf() {
        EmployeeType test1 = EmployeeType.valueOf("TEST1");
        EmployeeType enum1 = EmployeeType.valueOf("ENUM1");

        assertEquals(EmployeeType.TEST1, test1);
        assertEquals(EmployeeType.ENUM1, enum1);
    }

    @Test
    public void testEmployeeTypeName() {
        assertEquals("TEST1", EmployeeType.TEST1.name());
        assertEquals("ENUM1", EmployeeType.ENUM1.name());
    }

    @Test
    public void testEmployeeTypeOrdinal() {
        assertEquals(0, EmployeeType.TEST1.ordinal());
        assertEquals(1, EmployeeType.ENUM1.ordinal());
    }

    @Test
    @Rollback(false)
    public void testEmployeeTypeWithEmployeeENUM1() {
        Employee emp = new Employee();
        emp.setName("Test Employee ENUM1");
        emp.setSalary(50000);
        emp.type = EmployeeType.ENUM1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(EmployeeType.ENUM1, found.type);
        assertEquals(EmployeeType.ENUM1, found.getType());
    }

    @Test
    @Rollback(false)
    public void testEmployeeTypeWithEmployeeTEST1() {
        Employee emp = new Employee();
        emp.setName("Test Employee TEST1");
        emp.setSalary(60000);
        emp.setType(EmployeeType.TEST1);

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertNotNull(found);
        assertEquals(EmployeeType.TEST1, found.type);
        assertEquals(EmployeeType.TEST1, found.getType());
    }

    @Test
    @Rollback(false)
    public void testEmployeeTypeUpdate() {
        Employee emp = new Employee();
        emp.setName("Type Change Test");
        emp.setSalary(55000);
        emp.type = EmployeeType.TEST1;

        em.persist(emp);
        em.flush();

        Employee found = em.find(Employee.class, emp.getId());
        assertEquals(EmployeeType.TEST1, found.type);

        found.type = EmployeeType.ENUM1;
        em.flush();

        Employee updated = em.find(Employee.class, emp.getId());
        assertEquals(EmployeeType.ENUM1, updated.type);
    }

    @Test
    public void testEmployeeTypeEquality() {
        EmployeeType type1 = EmployeeType.ENUM1;
        EmployeeType type2 = EmployeeType.ENUM1;
        EmployeeType type3 = EmployeeType.TEST1;

        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
        assertSame(type1, type2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeTypeInvalidValueOf() {
        EmployeeType.valueOf("INVALID_TYPE");
    }
}
