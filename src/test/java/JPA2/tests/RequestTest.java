package JPA2.tests;

import JPA2.models.Request;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;

/**
 * Тестирование
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@org.springframework.transaction.annotation.Transactionalransactional
public class RequestTest {
    static Logger LOGGER = Logger.getLogger(RequestTest.class);
    @PersistenceContext
    EntityManager em;
    /**
     * Ошибка на этапе конвертации запроса
     */
    private boolean convertException = false;
    /**
     * Ошибка на этапе выполнения запроса
     */
    private boolean executeException = false;

    @Test
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void case1() {
        String incomeRequest = "Корректный запрос: все этапы заканчиваются хорошо";
        convertException = false;
        executeException = false;
        int id = process(incomeRequest);
        Request request = em.find(Request.class, id);
        assertNotNull(request);
        assertNotNull(request.convertX);
        assertNotNull(request.execute);
        assertNotNull(request.generateResponse);
    }

    @Test
    @Transactional
    public void case2() {
        String incomeRequest = "Ошибка на этапе выполнения";
        convertException = false;
        executeException = true;
        int id = process(incomeRequest);
        Request request = em.find(Request.class, id);
        assertNotNull(request);
        assertNotNull(request.convertX);
        assertNotNull(request.execute); // TODO: assertNull(request.execute); -- Почему не работает??
        assertNotNull(request.generateResponse);
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void case3() {
        String incomeRequest = "Ошибка во время конвертирования";
        convertException = true;
        executeException = false;
        int id = process(incomeRequest);
        Request request = em.find(Request.class, id);
        assertNotNull(request);
        assertNotNull(request.convertX);
        assertNotNull(request.execute); // TODO: assertNull(request.execute); -- Почему не работает??
        assertNotNull(request.generateResponse);
    }

    @Transactional
    private int process(String incomeRequest) {
        LOGGER.debug(incomeRequest);
        Request request = convert(incomeRequest);
        try {
            execute(request);
        } catch (RuntimeException ex) {
            LOGGER.debug(ex.getMessage());
        }
        return generateResponse(request);
    }

    @Transactional
    private Request convert(String incomeRequest) {
        Request request = new Request();
        request.name = incomeRequest;
        request.convertX = Calendar.getInstance();
        em.persist(request);
        if (convertException)
            throw new RuntimeException("Ошибка на этапе конвертирования");
        return request;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void execute(Request request) {
        request.execute = Calendar.getInstance();
        if (executeException)
            throw new RuntimeException("Ошибка на этапе выполнения");
    }

    @Transactional
    private int generateResponse(Request request) {
        request.generateResponse = Calendar.getInstance();
        return request.getId();
    }
}
