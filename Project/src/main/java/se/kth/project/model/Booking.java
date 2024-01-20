package se.kth.project.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Booking {

    private Integer listId;
    private Integer studentId;
    private Integer coopId;
    private LocalDateTime selectedTime;
    private List<LocalDateTime> timeSlots;

}
