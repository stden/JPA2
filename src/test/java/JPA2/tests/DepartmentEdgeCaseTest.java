package JPA2.tests;

import JPA2.models.Department;
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
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Edge case tests for Department entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class DepartmentEdgeCaseTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testDepartmentWithNullName() {
        Department dept = new Department();
        dept.setId(7000);
        dept.setName(null);

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7000);
        assertNotNull(found);
        assertNull(found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithEmptyName() {
        Department dept = new Department();
        dept.setId(7001);
        dept.setName("");

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7001);
        assertNotNull(found);
        assertEquals("", found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithVeryLongName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            longName.append("Department");
        }

        Department dept = new Department();
        dept.setId(7002);
        dept.setName(longName.toString());

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7002);
        assertNotNull(found);
        assertEquals(longName.toString(), found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithSpecialCharacters() {
        Department dept = new Department();
        dept.setId(7003);
        dept.setName("R&D / IT @#$% Department");

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7003);
        assertNotNull(found);
        assertEquals("R&D / IT @#$% Department", found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithUnicodeCharacters() {
        Department dept = new Department();
        dept.setId(7004);
        dept.setName("部門 Отдел قسم");

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7004);
        assertNotNull(found);
        assertEquals("部門 Отдел قسم", found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentNameUpdate() {
        Department dept = new Department();
        dept.setId(7005);
        dept.setName("Old Name");

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7005);
        found.setName("New Name");
        em.flush();

        Department updated = em.find(Department.class, 7005);
        assertEquals("New Name", updated.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithNullEmployees() {
        Department dept = new Department();
        dept.setId(7006);
        dept.setName("No Employees");
        dept.setEmployees(null);

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7006);
        assertNotNull(found);
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithEmptyEmployees() {
        Department dept = new Department();
        dept.setId(7007);
        dept.setName("Empty Employees");
        dept.setEmployees(new ArrayList<>());

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 7007);
        assertNotNull(found);
        assertNotNull(found.getEmployees());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithManyEmployees() {
        Department dept = new Department();
        dept.setId(7008);
        dept.setName("Large Department");
        em.persist(dept);

        for (int i = 0; i < 50; i++) {
            Employee emp = new Employee();
            emp.setName("Employee " + i);
            emp.setSalary(40000 + i * 1000);
            emp.type = EmployeeType.ENUM1;
            emp.setDepartment(dept);
            em.persist(emp);
        }

        em.flush();

        Department found = em.find(Department.class, 7008);
        assertNotNull(found);
        assertEquals("Large Department", found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentMultipleUpdates() {
        Department dept = new Department();
        dept.setId(7009);
        dept.setName("Initial");

        em.persist(dept);
        em.flush();

        for (int i = 0; i < 10; i++) {
            Department found = em.find(Department.class, 7009);
            found.setName("Update " + i);
            em.flush();
        }

        Department final1 = em.find(Department.class, 7009);
        assertEquals("Update 9", final1.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentIdBoundaries() {
        Department dept1 = new Department();
        dept1.setId(Integer.MIN_VALUE);
        dept1.setName("Min ID");
        em.persist(dept1);

        Department dept2 = new Department();
        dept2.setId(Integer.MAX_VALUE);
        dept2.setName("Max ID");
        em.persist(dept2);

        em.flush();

        Department found1 = em.find(Department.class, Integer.MIN_VALUE);
        Department found2 = em.find(Department.class, Integer.MAX_VALUE);

        assertNotNull(found1);
        assertNotNull(found2);
        assertEquals("Min ID", found1.getName());
        assertEquals("Max ID", found2.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentRepeatedFlush() {
        Department dept = new Department();
        dept.setId(7010);
        dept.setName("Repeated Flush");

        em.persist(dept);
        em.flush();
        em.flush();
        em.flush();

        Department found = em.find(Department.class, 7010);
        assertNotNull(found);
        assertEquals("Repeated Flush", found.getName());
    }
}
