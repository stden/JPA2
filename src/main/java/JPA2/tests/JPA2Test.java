package JPA2.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

    @Test
    public void test1() {
        // ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:");


        // Obtaining an Entity Manager
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("EmployeeService");


    }
}
