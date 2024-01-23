package se.kth.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;
/**
 * Represents a course entity in the application.
 * <p>
 * This class is annotated as a JPA entity and maps to the "courses" table in the database.
 * It defines the structure of the course table, including an identifier, title, and a list of users associated with the course.
 * The association with users is represented as a many-to-many relationship.
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 * @see jakarta.persistence.Id
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.GenerationType
 * @see jakarta.persistence.Column
 * @see jakarta.persistence.ManyToMany
 * @see jakarta.persistence.CascadeType
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "courses")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "courses", cascade = CascadeType.REMOVE)
    private List<UserEntity> users = new ArrayList<>();
}
