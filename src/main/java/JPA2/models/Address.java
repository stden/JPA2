package JPA2.models;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Embeddable Address Type
 * Запись, встроенная в другую. Вставляются дополнительные колонки.
 */
@Embeddable
@Access(AccessType.FIELD)
public class Address {
    private String street;
    private String city;
    private String state;
    @Column(name = "ZIP_CODE")
    private String zip;

    public Address() {
        city = "Санкт-Петербург";
        street = "Северный проспект";
        state = "СПб и ЛО";
        zip = "194295";
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}