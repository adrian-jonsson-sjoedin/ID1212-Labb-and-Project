package se.kth.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lists")
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "course_id")
    private int courseId;

    @Column(name = "user_id")
    private int userId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    //    @Temporal(TemporalType.TIMESTAMP)
//    @Column(nullable = false)
//    private Timestamp start;
    private LocalDateTime start;

    @Column(nullable = false)
    private int intervall; //interval is a reserved keyword in MySQL

    @Column(nullable = false)
    private int max_slots;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    private Set<Reservation> reservations = new HashSet<>();
}
