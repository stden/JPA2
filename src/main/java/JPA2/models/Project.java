package JPA2.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Collection;

/**
 * Проект
 */
@Entity
public class Project {
    @Id
    private int id;
    private String name;
    @ManyToMany(mappedBy = "projects")
    private Collection<Employee> employees;
}