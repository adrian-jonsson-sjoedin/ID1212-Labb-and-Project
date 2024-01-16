package se.kth.project.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class Booking {

    private Integer listId;
    private Integer studentId;
    private Integer coopId;
    private int maxSlots;
    private ArrayList<LocalDateTime> timeSlots;
    private boolean[] slotIsBooked;
    private String location;
    private String description;


    public Booking(Integer listId,
                   Integer studentId,
                   int maxSlots,
                   LocalDateTime startTime,
                   int timeInterval,
                   int[] sequence,
                   String location,
                   String description) {

        this(listId, studentId, null, maxSlots, startTime, timeInterval, sequence, location, description);
    }

    public Booking(Integer listId,
                   Integer studentId,
                   Integer coopId,
                   int maxSlots,
                   LocalDateTime startTime,
                   int timeInterval,
                   int[] sequence,
                   String location,
                   String description) {

        this.listId = listId;
        this.studentId = studentId;
        this.coopId = coopId;
        this.maxSlots = maxSlots;
        this.location = location;
        this.description = description;
        this.timeSlots = setTimeSlots(startTime, timeInterval);
        populateSlotIsBooked(sequence);
    }

    public void bookSlot(int slotIndex){
        this.slotIsBooked[slotIndex] = true;
    }

    private ArrayList<LocalDateTime> setTimeSlots(LocalDateTime startTime, int timeInterval) {
        ArrayList<LocalDateTime> times = new ArrayList<>(this.maxSlots);
        times.add(startTime);
        for (int i = 1; i < this.maxSlots; i++) {
            LocalDateTime nexTimeSlot = times.get(i - 1).plusMinutes(timeInterval);
            times.add(nexTimeSlot);
        }
        return times;
    }

    //a 0 in the sequence array indicates no booking for that slot
    private void populateSlotIsBooked(int[] sequence) {
        for (int j : sequence) {
            if (j != 0) {
                this.slotIsBooked[j - 1] = true;
            }
        }
    }
}
