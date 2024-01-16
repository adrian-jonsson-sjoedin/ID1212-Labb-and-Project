package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.kth.project.model.ListEntity;

public interface ListRepository extends JpaRepository<ListEntity, Integer> {
    @Query("SELECT list.maxSlots FROM ListEntity list WHERE list.id=:listId")
    int findMaxSlotsById(@Param("listId") Integer listId);

}
