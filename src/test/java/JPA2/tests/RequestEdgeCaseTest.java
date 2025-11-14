package JPA2.tests;

import JPA2.models.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Edge case and transaction tests for Request entity
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test-context.xml"})
@Transactional
public class RequestEdgeCaseTest {

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(false)
    public void testRequestWithNullName() {
        Request request = new Request();
        request.setName(null);
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertNull(found.getName());
    }

    @Test
    @Rollback(false)
    public void testRequestWithEmptyName() {
        Request request = new Request();
        request.setName("");
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertEquals("", found.getName());
    }

    @Test
    @Rollback(false)
    public void testRequestWithVeryLongName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            longName.append("RequestName");
        }

        Request request = new Request();
        request.setName(longName.toString());
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertEquals(longName.toString(), found.getName());
    }

    @Test
    @Rollback(false)
    public void testRequestWithAllNullDates() {
        Request request = new Request();
        request.setName("Null Dates");
        request.setConvertX(null);
        request.setExecute(null);
        request.setGenerateResponse(null);

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertNull(found.getConvertX());
        assertNull(found.getExecute());
        assertNull(found.getGenerateResponse());
    }

    @Test
    @Rollback(false)
    public void testRequestWithPartialDates() {
        Request request = new Request();
        request.setName("Partial Dates");
        request.convertX = Calendar.getInstance();
        request.execute = null;
        request.generateResponse = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertNotNull(found.convertX);
        assertNull(found.execute);
        assertNotNull(found.generateResponse);
    }

    @Test
    @Rollback(false)
    public void testRequestWithFutureDates() {
        Request request = new Request();
        request.setName("Future Dates");

        Calendar future = Calendar.getInstance();
        future.set(2099, Calendar.DECEMBER, 31, 23, 59, 59);

        request.convertX = (Calendar) future.clone();
        request.execute = (Calendar) future.clone();
        request.generateResponse = (Calendar) future.clone();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertNotNull(found.convertX);
        assertNotNull(found.execute);
        assertNotNull(found.generateResponse);
    }

    @Test
    @Rollback(false)
    public void testRequestWithPastDates() {
        Request request = new Request();
        request.setName("Past Dates");

        Calendar past = Calendar.getInstance();
        past.set(1900, Calendar.JANUARY, 1, 0, 0, 0);

        request.convertX = (Calendar) past.clone();
        request.execute = (Calendar) past.clone();
        request.generateResponse = (Calendar) past.clone();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertNotNull(found.convertX);
    }

    @Test
    @Rollback(false)
    public void testRequestUpdateAllFields() {
        Request request = new Request();
        request.setName("Original");
        request.convertX = Calendar.getInstance();
        request.execute = Calendar.getInstance();
        request.generateResponse = Calendar.getInstance();

        em.persist(request);
        em.flush();

        int id = request.getId();

        Request found = em.find(Request.class, id);
        found.setName("Updated");

        Calendar newTime = Calendar.getInstance();
        newTime.set(2025, Calendar.JUNE, 15, 10, 30, 0);

        found.convertX = (Calendar) newTime.clone();
        found.execute = (Calendar) newTime.clone();
        found.generateResponse = (Calendar) newTime.clone();

        em.flush();

        Request updated = em.find(Request.class, id);
        assertEquals("Updated", updated.getName());
        assertNotNull(updated.convertX);
        assertNotNull(updated.execute);
        assertNotNull(updated.generateResponse);
    }

    @Test
    @Rollback(false)
    public void testRequestMultipleUpdates() {
        Request request = new Request();
        request.setName("Initial");
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        int id = request.getId();

        for (int i = 0; i < 10; i++) {
            Request found = em.find(Request.class, id);
            found.setName("Update " + i);
            em.flush();
        }

        Request final1 = em.find(Request.class, id);
        assertEquals("Update 9", final1.getName());
    }

    @Test
    @Rollback(false)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testRequestWithRequiresNewPropagation() {
        Request request = new Request();
        request.setName("Requires New");
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
    }

    @Test
    @Rollback(false)
    @Transactional(propagation = Propagation.NESTED)
    public void testRequestWithNestedPropagation() {
        Request request = new Request();
        request.setName("Nested Transaction");
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
    }

    @Test
    @Rollback(false)
    public void testRequestIdSetter() {
        Request request = new Request();
        request.setId(99999);

        assertEquals(99999, request.getId());
    }

    @Test
    @Rollback(false)
    public void testRequestTimestampPrecision() {
        Request request = new Request();
        request.setName("Timestamp Test");

        Calendar precise = Calendar.getInstance();
        precise.set(Calendar.MILLISECOND, 123);

        request.convertX = precise;

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertNotNull(found.convertX);
    }

    @Test
    @Rollback(false)
    public void testRequestWithSpecialCharacters() {
        Request request = new Request();
        request.setName("Special!@#$%^&*()_+-={}[]|:\";<>?,./");
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertEquals("Special!@#$%^&*()_+-={}[]|:\";<>?,./", found.getName());
    }

    @Test
    @Rollback(false)
    public void testRequestWithUnicodeCharacters() {
        Request request = new Request();
        request.setName("请求 リクエスト 요청");
        request.convertX = Calendar.getInstance();

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertEquals("请求 リクエスト 요청", found.getName());
    }

    @Test
    @Rollback(false)
    public void testRequestSequentialDates() {
        Request request = new Request();
        request.setName("Sequential Dates");

        Calendar time1 = Calendar.getInstance();

        Calendar time2 = (Calendar) time1.clone();
        time2.add(Calendar.MINUTE, 5);

        Calendar time3 = (Calendar) time2.clone();
        time3.add(Calendar.MINUTE, 5);

        request.convertX = time1;
        request.execute = time2;
        request.generateResponse = time3;

        em.persist(request);
        em.flush();

        Request found = em.find(Request.class, request.getId());
        assertNotNull(found);
        assertTrue(found.execute.after(found.convertX));
        assertTrue(found.generateResponse.after(found.execute));
    }
}
