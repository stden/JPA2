package JPA2.tests;

import JPA2.models.Employee;
import JPA2.models.EmployeeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Примеры из книги
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class JPA2Test {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public Employee createEmployee(String name, long salary) {
        Employee emp = new Employee();
        emp.type = EmployeeType.ENUM1;
        emp.setName(name);
        emp.setSalary(salary);
        emp.phoneNumbers = new HashMap<String, String>();
        emp.phoneNumbers.put("Домашний", "5113195");
        em.persist(emp); // Сохраняем в persist-контексте, чтобы где-то кроме прямого указателя можно было получить
        return emp;
    }

    @Test
    @Rollback(false)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void test1() throws Exception {
        removeIfExists(239);
        removeIfExists(240);
        removeIfExists(158);

        Employee denis = getEmployee();

        // Finding an Entity
        Employee empl1 = em.find(Employee.class, denis.getId());

        assertEquals(denis.getName(), empl1.getName());

        // Updating an Entity
        Employee emp2 = em.find(Employee.class, denis.getId());
        emp2.setSalary(emp2.getSalary() + 1000);

        Employee empl2a = em.find(Employee.class, denis.getId());
        assertEquals(2024, empl2a.getSalary());
        assertEquals(2024, denis.getSalary());

        Employee e2 = createEmployee("Denis2", 1024);

        // Removing an Entity
        Employee emp = em.find(Employee.class, denis.getId());
        em.remove(emp);

        // Снова ищем
        Employee empl2 = em.find(Employee.class, denis.getId());
        assertNull(empl2);

        // Beginning and Committing an EntityTransaction
        createEmployee("John Doe", 45000);
    }

    private void removeIfExists(int id) {
        Employee e = em.find(Employee.class, id);
        if (e != null)
            em.remove(e);
    }

    @Transactional(propagation = Propagation.NESTED)
    private Employee getEmployee() {
        // Создаём экземпляр
        Employee e = createEmployee("Denis", 1024);
        em.flush();
        return e;
    }
}
