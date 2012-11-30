package JPA2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Collection;

@Entity
public class Department {
    @Id
    private int id;
    private String name;
    @OneToMany(mappedBy = "department")
    private Collection<Employee> employees;
}