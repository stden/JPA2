package JPA2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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