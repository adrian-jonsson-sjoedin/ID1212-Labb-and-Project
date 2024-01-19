package se.kth.project.model;

import lombok.Data;
import se.kth.project.util.CalculateFreeSlotsAndTime;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class Booking {

    private Integer listId;
    private Integer studentId;
    private Integer coopId;
    private int maxSlots;
    private LocalDateTime selectedTime;
    private ArrayList<LocalDateTime> timeSlots;
    private boolean[] slotIsBooked;
    private String location;
    private String description;


    public Booking(Integer listId,
                   int maxSlots,
                   LocalDateTime startTime,
                   int timeInterval,
                   int[] sequence,
                   String location,
                   String description) {

        this.listId = listId;
        this.maxSlots = maxSlots;
        this.location = location;
        this.description = description;
        this.timeSlots = CalculateFreeSlotsAndTime.getAvailableTimeSlots(startTime, timeInterval, sequence);
        this.slotIsBooked = CalculateFreeSlotsAndTime.isSlotBooked(sequence);
    }

    public void bookSlot(int slotIndex){
        this.slotIsBooked[slotIndex] = true;
    }

}
