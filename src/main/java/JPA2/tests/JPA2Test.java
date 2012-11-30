package JPA2.tests;

import JPA2.models.Employee;
import JPA2.models.EmployeeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Примеры из книги
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
@TransactionConfiguration(transactionManager = "transactionManager")
@Transactional(
        propagation = Propagation.REQUIRES_NEW,
        isolation = Isolation.DEFAULT,
        readOnly = false)
public class JPA2Test {
    @PersistenceContext
    EntityManager em;


    @Transactional
    public Employee createEmployee(int id, String name, long salary) {
        Employee emp = new Employee();
        emp.type = EmployeeType.ENUM1;
        emp.setName(name);
        emp.setSalary(salary);
        em.persist(emp);
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

        Employee e2 = createEmployee(240, "Denis2", 1024);

        // Removing an Entity
        Employee emp = em.find(Employee.class, denis.getId());
        em.remove(emp);

        // Снова ищем
        Employee empl2 = em.find(Employee.class, denis.getId());
        assertNull(empl2);

        // Beginning and Committing an EntityTransaction
        createEmployee(158, "John Doe", 45000);
    }

    private void removeIfExists(int id) {
        Employee e = em.find(Employee.class, id);
        if (e != null)
            em.remove(e);
    }

    @Transactional(propagation = Propagation.NESTED)
    private Employee getEmployee() {
        // Создаём экземпляр
        Employee e = createEmployee(239, "Denis", 1024);
        em.flush();
        return e;
    }
}
