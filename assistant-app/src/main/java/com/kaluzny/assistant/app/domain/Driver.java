package com.kaluzny.assistant.app.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * @author Oleg Kaluzny
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drivers", schema = "ms_assistant")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drivers_seq")
    @SequenceGenerator(name = "drivers_seq", sequenceName = "drivers_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "truck_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Truck truck;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @OrderBy("id desc")
    private List<Address> addresses = new ArrayList<>();
}
