package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.model.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing {@link se.kth.project.model.ReservationEntity} entities.
 * Provides CRUD operations and custom queries for reservations.
 */
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    /**
     * Retrieves a list of reservation entities associated with the specified user ID.
     *
     * @param userId The ID of the user for whom reservations are to be retrieved.
     * @return A list of reservation entities associated with the specified user ID.
     */
    List<ReservationEntity> findByUserId(int userId);

    /**
     * Retrieves a list of reservation entities associated with the specified cooperative user ID.
     *
     * @param coopId The ID of the cooperative user for whom reservations are to be retrieved.
     * @return A list of reservation entities associated with the specified cooperative user ID.
     */
    List<ReservationEntity> findByCoopId_Id(int coopId);

    /**
     * Counts the number of reservations associated with a reservation list based on the list ID.
     *
     * @param listId The ID of the reservation list for which the count is to be determined.
     * @return The number of reservations associated with the specified reservation list ID.
     */
    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.list.id=:listId")
    int countReservationsForListByListId(@Param("listId") Integer listId);

    /**
     * Retrieves the sequence of reservation dates for a reservation list based on the list ID.
     *
     * @param listId The ID of the reservation list for which the sequence is to be retrieved.
     * @return The list of reservation dates associated with the specified reservation list ID.
     */
    @Query("SELECT r.sequence FROM ReservationEntity r WHERE r.list.id=:listId")
    List<LocalDateTime> getSequenceForReservationByListId(@Param("listId") Integer listId);

    /**
     * Deletes reservations associated with a user ID (either as a booker or a cooperative user).
     *
     * @param userId The ID of the user for whom reservations are to be deleted.
     */
    @Modifying
    @Query("DELETE FROM ReservationEntity r WHERE r.user.id = :userId OR r.coopId.id = :userId")
    void deleteReservationsByUserId(@Param("userId") Integer userId);

    /**
     * Deletes reservations associated with a reservation list based on the list ID.
     *
     * @param listId The ID of the reservation list for which reservations are to be deleted.
     */
    void deleteByListId(Integer listId);
}
