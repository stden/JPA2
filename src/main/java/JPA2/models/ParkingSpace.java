package JPA2.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class ParkingSpace {
    @Id
    private int id;
    private int lot;
    private String location;
    @OneToOne(mappedBy = "parkingSpace")
    private Employee employee;
}
