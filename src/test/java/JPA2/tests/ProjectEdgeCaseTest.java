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
 * Edge case tests for Project entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class ProjectEdgeCaseTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testProjectWithNullName() {
        Project project = new Project();
        project.setId(8000);
        project.setName(null);

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8000);
        assertNotNull(found);
        assertNull(found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectWithEmptyName() {
        Project project = new Project();
        project.setId(8001);
        project.setName("");

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8001);
        assertNotNull(found);
        assertEquals("", found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectWithVeryLongName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 150; i++) {
            longName.append("ProjectName");
        }

        Project project = new Project();
        project.setId(8002);
        project.setName(longName.toString());

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8002);
        assertNotNull(found);
        assertEquals(longName.toString(), found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectWithSpecialCharacters() {
        Project project = new Project();
        project.setId(8003);
        project.setName("Project-2024 (Q1) @Company #123");

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8003);
        assertNotNull(found);
        assertEquals("Project-2024 (Q1) @Company #123", found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectWithUnicodeCharacters() {
        Project project = new Project();
        project.setId(8004);
        project.setName("项目 プロジェクト 프로젝트");

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8004);
        assertNotNull(found);
        assertEquals("项目 プロジェクト 프로젝트", found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectNameUpdate() {
        Project project = new Project();
        project.setId(8005);
        project.setName("Version 1.0");

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8005);
        found.setName("Version 2.0");
        em.flush();

        Project updated = em.find(Project.class, 8005);
        assertEquals("Version 2.0", updated.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectWithNullEmployees() {
        Project project = new Project();
        project.setId(8006);
        project.setName("No Employees");
        project.setEmployees(null);

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8006);
        assertNotNull(found);
    }

    @Test
    @Rollback(false)
    public void testProjectWithEmptyEmployees() {
        Project project = new Project();
        project.setId(8007);
        project.setName("Empty Employees");
        project.setEmployees(new ArrayList<>());

        em.persist(project);
        em.flush();

        Project found = em.find(Project.class, 8007);
        assertNotNull(found);
        assertNotNull(found.getEmployees());
    }

    @Test
    @Rollback(false)
    public void testProjectWithManyEmployees() {
        Project project = new Project();
        project.setId(8008);
        project.setName("Large Project");
        em.persist(project);

        for (int i = 0; i < 30; i++) {
            Employee emp = new Employee();
            emp.setName("Team Member " + i);
            emp.setSalary(50000 + i * 500);
            emp.type = EmployeeType.ENUM1;

            Collection<Project> projects = new ArrayList<>();
            projects.add(project);
            emp.setProjects(projects);

            em.persist(emp);
        }

        em.flush();

        Project found = em.find(Project.class, 8008);
        assertNotNull(found);
        assertEquals("Large Project", found.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectMultipleUpdates() {
        Project project = new Project();
        project.setId(8009);
        project.setName("Sprint 1");

        em.persist(project);
        em.flush();

        for (int i = 2; i <= 10; i++) {
            Project found = em.find(Project.class, 8009);
            found.setName("Sprint " + i);
            em.flush();
        }

        Project final1 = em.find(Project.class, 8009);
        assertEquals("Sprint 10", final1.getName());
    }

    @Test
    @Rollback(false)
    public void testProjectIdBoundaries() {
        Project p1 = new Project();
        p1.setId(Integer.MIN_VALUE);
        p1.setName("Min ID Project");
        em.persist(p1);

        Project p2 = new Project();
        p2.setId(Integer.MAX_VALUE);
        p2.setName("Max ID Project");
        em.persist(p2);

        em.flush();

        Project found1 = em.find(Project.class, Integer.MIN_VALUE);
        Project found2 = em.find(Project.class, Integer.MAX_VALUE);

        assertNotNull(found1);
        assertNotNull(found2);
    }

    @Test
    @Rollback(false)
    public void testProjectRepeatedFlush() {
        Project project = new Project();
        project.setId(8010);
        project.setName("Flush Test");

        em.persist(project);
        em.flush();
        em.flush();
        em.flush();

        Project found = em.find(Project.class, 8010);
        assertNotNull(found);
        assertEquals("Flush Test", found.getName());
    }
}
