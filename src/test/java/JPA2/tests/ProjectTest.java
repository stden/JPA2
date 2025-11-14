package JPA2.tests;

import JPA2.models.Employee;
import JPA2.models.EmployeeType;
import JPA2.models.Project;
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
 * Comprehensive tests for Project entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class ProjectTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testProjectBasicFields() {
        Project project = new Project();
        project.setId(3000);
        project.setName("Website Redesign");

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 3000);
        assertNotNull(found);
        assertEquals(3000, found.getId());
        assertEquals("Website Redesign", found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectWithEmployees() {
        Project project = new Project();
        project.setId(3001);
        project.setName("Mobile App Development");
        em.persist(project);

        Employee emp1 = new Employee();
        emp1.setName("Developer One");
        emp1.setSalary(70000);
        emp1.type = EmployeeType.ENUM1;

        Collection<Project> projects1 = new ArrayList<>();
        projects1.add(project);
        emp1.setProjects(projects1);
        em.persist(emp1);

        Employee emp2 = new Employee();
        emp2.setName("Developer Two");
        emp2.setSalary(75000);
        emp2.type = EmployeeType.TEST1;

        Collection<Project> projects2 = new ArrayList<>();
        projects2.add(project);
        emp2.setProjects(projects2);
        em.persist(emp2);

        em.flush();

        Project found = em.find(Project.class, 3001);
        assertNotNull(found);
        assertEquals("Mobile App Development", found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectGettersSetters() {
        Project project = new Project();
        project.setId(3002);
        project.setName("Database Migration");

        Collection<Employee> employees = new ArrayList<>();
        project.setEmployees(employees);

        assertEquals(3002, project.getId());
        assertEquals("Database Migration", project.getName());
        assertNotNull(project.getEmployees());
    }

    @Test
    @Rollback(false)
    public void testProjectNameUpdate() {
        Project project = new Project();
        project.setId(3003);
        project.setName("Original Project");
        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 3003);
        found.setName("Updated Project Name");
        em.flush();

        Project updated = em.find(Project.class, 3003);
        assertEquals("Updated Project Name", updated.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectMultipleEmployees() {
        Project project = new Project();
        project.setId(3004);
        project.setName("Team Project");
        em.persist(project);

        for (int i = 0; i < 5; i++) {
            Employee emp = new Employee();
            emp.setName("Team Member " + i);
            emp.setSalary(50000 + i * 5000);
            emp.type = i % 2 == 0 ? EmployeeType.ENUM1 : EmployeeType.TEST1;

            Collection<Project> projects = new ArrayList<>();
            projects.add(project);
            emp.setProjects(projects);
            em.persist(emp);
        }

        em.flush();

        Project found = em.find(Project.class, 3004);
        assertNotNull(found);
        assertEquals("Team Project", found.getName());
    }
}
