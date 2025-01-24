package JPA2.models;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Сущность для тестирования вложенных транзакций
 */
@Entity
public class Request {
    public String name;
    public Calendar convertX;
    public Calendar execute;
    public Calendar generateResponse;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
package JPA2.models;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    public String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar convertX;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar execute;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar generateResponse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
