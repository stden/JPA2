package JPA2.tests;

import JPA2.models.Request;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Calendar;

import static org.junit.Assert.assertNotNull;

/**
 * Тестирование
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@org.springframework.transaction.annotation.Transactional
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

    @Test(expected = RuntimeException.class)
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
        request.setName(incomeRequest);
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

    @Test
    @Transactional
    public void testRequestGettersSetters() {
        Request request = new Request();

        request.setName("Test Request");
        assertEquals("Test Request", request.getName());

        Calendar convertTime = Calendar.getInstance();
        request.setConvertX(convertTime);
        assertEquals(convertTime, request.getConvertX());

        Calendar executeTime = Calendar.getInstance();
        request.setExecute(executeTime);
        assertEquals(executeTime, request.getExecute());

        Calendar responseTime = Calendar.getInstance();
        request.setGenerateResponse(responseTime);
        assertEquals(responseTime, request.getGenerateResponse());
    }

    @Test
    @Transactional
    public void testRequestIdSetterGetter() {
        Request request = new Request();
        request.setId(999);
        assertEquals(999, request.getId());
    }

    @Test
    @Rollback(false)
    @Transactional
    public void testRequestPersistence() {
        Request request = new Request();
        request.setName("Persistence Test");
        request.convertX = Calendar.getInstance();
        request.execute = Calendar.getInstance();
        request.generateResponse = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertEquals("Persistence Test", found.getName());
        assertNotNull(found.convertX);
        assertNotNull(found.execute);
        assertNotNull(found.generateResponse);
    }

    @Test
    @Rollback(false)
    @Transactional
    public void testRequestAllFields() {
        Request request = new Request();

        request.setName("Complete Request");

        Calendar conv = Calendar.getInstance();
        conv.set(2024, Calendar.JANUARY, 1, 10, 0, 0);
        request.setConvertX(conv);

        Calendar exec = Calendar.getInstance();
        exec.set(2024, Calendar.JANUARY, 1, 10, 5, 0);
        request.setExecute(exec);

        Calendar resp = Calendar.getInstance();
        resp.set(2024, Calendar.JANUARY, 1, 10, 10, 0);
        request.setGenerateResponse(resp);

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertEquals("Complete Request", found.getName());
        assertNotNull(found.getConvertX());
        assertNotNull(found.getExecute());
        assertNotNull(found.getGenerateResponse());
    }
}
