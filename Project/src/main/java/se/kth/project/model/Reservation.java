package se.kth.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private List list;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @Column(name = "coop_id")
//    private int coopId;
    @Column(nullable = false)
    private int sequence;
}
