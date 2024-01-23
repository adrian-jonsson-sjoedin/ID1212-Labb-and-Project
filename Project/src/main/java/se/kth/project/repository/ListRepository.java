package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.ListEntity;

/**
 * Repository interface for managing {@link se.kth.project.model.ListEntity} entities.
 * Provides CRUD operations and custom queries for reservation lists.
 */
public interface ListRepository extends JpaRepository<ListEntity, Integer> {
    /**
     * Retrieves a reservation list entity by the ID of the associated course.
     *
     * @param courseId The ID of the course associated with the reservation list.
     * @return The reservation list entity associated with the specified course ID, or {@code null} if not found.
     */
    ListEntity findByCourse_Id(int courseId);
}
