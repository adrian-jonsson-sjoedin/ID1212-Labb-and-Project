package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.model.ReservationEntity;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

     List<ReservationEntity> findByUserId(int userId);

     @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.list.id=:listId")
     int countReservationsForListByListId(@Param("listId") Integer listId);

     @Query("SELECT r.sequence FROM ReservationEntity r WHERE r.id=:reservationId")
     int getSequenceForReservationByReservationId(@Param("reservationId") Integer reservationId);

     List<ReservationEntity> findAllByListId(Integer listId);

     void deleteById(int id);
}
