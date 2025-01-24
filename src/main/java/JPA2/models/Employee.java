
package JPA2.models;

import javax.persistence.*;
import java.util.*;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private long salary;

    @Temporal(TemporalType.DATE)
    private Calendar dob;

    @Temporal(TemporalType.DATE)
    @Column(name = "S_DATE")
    private Date startDate;

    @Enumerated(EnumType.STRING)
    public EmployeeType type;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne
    @JoinColumn(name = "parking_space_id")
    private ParkingSpace parkingSpace;

    @ManyToMany
    private Collection<Project> projects;

    @Embedded
    private Address address;

    @ElementCollection
    @CollectionTable(name = "EMP_PHONE")
    @MapKeyColumn(name = "PHONE_TYPE")
    @Column(name = "PHONE_NUM")
    public Map<String, String> phoneNumbers;

    public Employee() {
    }

    public Employee(int id) {
        this.id = id;
    }

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

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
