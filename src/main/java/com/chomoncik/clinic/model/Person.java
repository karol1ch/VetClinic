package com.chomoncik.clinic.model;

import com.chomoncik.clinic.model.DTO.PersonRequestDTO;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private final Long personId;

    @Column
    private final String name;

    @Column
    private final String surname;

    @Column
    private final String address;

    @Column
    private final String contact;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private final Set<Animal> animalSet;

    public Person() {
        this.personId = 0L;
        this.name = null;
        this.surname = null;
        this.address = null;
        this.contact = null;
        this.animalSet = new HashSet<>();
    }

    public Person(PersonRequestDTO personRequestDTO) {
        this.personId = 0L;
        this.name = personRequestDTO.getName();
        this.surname = personRequestDTO.getSurname();
        this.address = personRequestDTO.getAddress();
        this.contact = personRequestDTO.getContact();
        this.animalSet = new HashSet<>();
    }
}
