
package JPA2.models;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    public String name;
    public Calendar convertX;
    public Calendar execute;
    public Calendar generateResponse;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
