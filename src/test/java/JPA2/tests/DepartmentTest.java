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
 * Comprehensive tests for Department entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class DepartmentTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testDepartmentBasicFields() {
        Department dept = new Department();
        dept.setId(1000);
        dept.setName("HR Department");

        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 1000);
        assertNotNull(found);
        assertEquals(1000, found.getId());
        assertEquals("HR Department", found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentWithEmployees() {
        Department dept = new Department();
        dept.setId(1001);
        dept.setName("IT Department");
        em.persist(dept);

        Employee emp1 = new Employee();
        emp1.setName("Employee One");
        emp1.setSalary(50000);
        emp1.type = EmployeeType.ENUM1;
        emp1.setDepartment(dept);
        em.persist(emp1);

        Employee emp2 = new Employee();
        emp2.setName("Employee Two");
        emp2.setSalary(60000);
        emp2.type = EmployeeType.TEST1;
        emp2.setDepartment(dept);
        em.persist(emp2);

        em.flush();

        Department found = em.find(Department.class, 1001);
        assertNotNull(found);
        assertEquals("IT Department", found.getName());
    }

    @Test
    @Rollback(false)
    public void testDepartmentGettersSetters() {
        Department dept = new Department();
        dept.setId(1002);
        dept.setName("Finance");

        Collection<Employee> employees = new ArrayList<>();
        dept.setEmployees(employees);

        assertEquals(1002, dept.getId());
        assertEquals("Finance", dept.getName());
        assertNotNull(dept.getEmployees());
    }

    @Test
    @Rollback(false)
    public void testDepartmentNameUpdate() {
        Department dept = new Department();
        dept.setId(1003);
        dept.setName("Original Name");
        em.persist(dept);
        em.flush();

        Department found = em.find(Department.class, 1003);
        found.setName("Updated Name");
        em.flush();

        Department updated = em.find(Department.class, 1003);
        assertEquals("Updated Name", updated.getName());
    }
}
