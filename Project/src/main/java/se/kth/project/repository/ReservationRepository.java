package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

     List<ReservationEntity> findByUserId(int userId);

     List<ReservationEntity> findByCoopId_Id(int coopId);



     @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.list.id=:listId")
     int countReservationsForListByListId(@Param("listId") Integer listId);

     @Query("SELECT r.sequence FROM ReservationEntity r WHERE r.list.id=:listId")
     List<LocalDateTime> getSequenceForReservationByListId(@Param("listId") Integer listId);

     @Modifying
     @Query("DELETE FROM ReservationEntity r WHERE r.user.id = :userId OR r.coopId.id = :userId")
     void deleteReservationsByUserId(@Param("userId") Integer userId);

     void deleteByListId(Integer listId);
}
