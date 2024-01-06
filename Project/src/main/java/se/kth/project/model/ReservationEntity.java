package se.kth.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // vilken listan är det som bokningen/reservationen hör till.
    @JoinColumn(name = "list_id")
    private ListEntity list;

    @ManyToOne // vem är (studenten) som gjort bokningen
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne // vem är (eventuell) medarbetare (också student)
    @JoinColumn(name = "coop_id", nullable = true)
    private UserEntity coopId; // second user id fk
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    // tidsintervallet mellan varje redovisning i minuter
    private LocalDateTime start;
    // sequence börjar med 0 -> max_slots, första bokning
    // är starttid, nästa är starttid + interval (från lists)
    @Column(nullable = false)
    private int sequence;
}
