package se.kth.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
/**
 * Represents a list entity in the application.
 * <p>
 * This class is annotated as a JPA entity and maps to the "lists" table in the database.
 * It defines the structure of the list table and its relationships with other entities,
 * such as the course it belongs to, the user who created the list, and other attributes like description, location,
 * start time, interval between presentations, and maximum slots available for reservations.
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
@Table(name = "lists")
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne // FK to what course the list belongs to
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne // Which admin created the list
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    // the time between each slot in minutes
    private LocalDateTime start;

    @Column(nullable = false)
    private int intervall; //interval is a reserved keyword in MySQL

    @Column(name="max_slots", nullable = false)
    private int maxSlots;

}
