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
}