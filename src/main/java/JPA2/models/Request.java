package JPA2.models;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "requests")
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Calendar getConvertX() {
        return convertX;
    }
    
    public void setConvertX(Calendar convertX) {
        this.convertX = convertX;
    }
    
    public Calendar getExecute() {
        return execute;
    }
    
    public void setExecute(Calendar execute) {
        this.execute = execute;
    }
    
    public Calendar getGenerateResponse() {
        return generateResponse;
    }
    
    public void setGenerateResponse(Calendar generateResponse) {
        this.generateResponse = generateResponse;
    }
}
package JPA2.models;

import javax.persistence.*;
import java.util.Calendar;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar convertX;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar execute;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar generateResponse;

    public int getId() {
        return id;
    }
}
