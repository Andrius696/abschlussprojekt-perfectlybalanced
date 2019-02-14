package de.hhu.abschlussprojektverleihplattform.model;

import javax.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Embeddable
public class AddressEntity {

    private String street;
    private int housenumber;
    private int postcode;
    private String city;


    public AddressEntity() {

    }

    public AddressEntity(String street, int housenumber, int postcode, String city) {
        this.street = street;
        this.housenumber = housenumber;
        this.postcode = postcode;
        this.city = city;
    }
}
