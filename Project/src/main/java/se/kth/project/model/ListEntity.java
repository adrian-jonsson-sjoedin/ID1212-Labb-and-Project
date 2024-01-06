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
@Table(name = "lists")
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // FK till vilken kurs som listan ska höra till
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne // Anger vilken av administratörerna som har skapat bokningslistan
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String list;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    // tidsintervallet mellan varje redovisning i minuter
    private LocalDateTime start;

    @Column(nullable = false)
    private int intervall; //interval is a reserved keyword in MySQL

    @Column(name="max_slots", nullable = false)
    private int maxSlots;

    /*
    Assistent Beda skapar en zoom-bokningslista för 8 labbar med 15 minuters
    mellanrum på ID1212 med titeln "labbar" som börjar 23:e dec kl 10:15
    INSERT INTO lists(course_id, user_id, description, location, start,
    interval,max_slots) VALUES (1, 2, 'labbar', 'zoom', TIMESTAMP('2023-12-23
    10:15:00.00'),15,8);
    INSERT INTO lists(course_id, user_id, description, location, start,
    interval,max_slots) VALUES (1, 2,'projekt', 'Ka-309', TIMESTAMP('2023-12-23
    13:15:00.00'),30,4);
     */
}
