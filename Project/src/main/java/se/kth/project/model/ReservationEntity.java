package se.kth.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a reservation entity in the application.
 * <p>
 * This class is annotated as a JPA entity and maps to the "reservations" table in the database.
 * It defines the structure of the reservation table and the relationships with other entities,
 * such as the list (reservation list), the user who made the reservation, and an optional cooperator (another user),
 * and the time.
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 * @see jakarta.persistence.Id
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.ManyToOne
 * @see jakarta.persistence.JoinColumn
 * @see java.time.LocalDateTime
 */
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

    @ManyToOne // which (reservation) list does this reservation/booking belong to
    @JoinColumn(name = "list_id")
    private ListEntity list;

    @ManyToOne // which student is the reservation for
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne // an optional cooperator (another user)
    @JoinColumn(name = "coop_id", nullable = true)
    private UserEntity coopId; // second user id fk

    //the time of the booked slot
    @Column(nullable = false)
    private LocalDateTime sequence;
}
