package se.kth.project.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CalculateFreeSlotsAndTime {

    public static ArrayList<LocalDateTime> getAvailableTimeSlots(LocalDateTime startTime, int timeInterval, int[] sequence) {
        boolean[] slotIsBooked = isSlotBooked(sequence);
        ArrayList<LocalDateTime> times = new ArrayList<>(sequence.length);
        times.add(startTime);
        for (int i = 1; i < sequence.length; i++) {
            LocalDateTime nexTimeSlot = times.get(i - 1).plusMinutes(timeInterval);
            times.add(nexTimeSlot);
        }
        ArrayList<LocalDateTime> availableTimeSlots = new ArrayList<>();
        int i = 0;
        for (boolean slot : slotIsBooked){
            if(!slot){
                LocalDateTime timeSlot = times.get(i);
                availableTimeSlots.add(timeSlot);
            }
            i++;
        }
        return availableTimeSlots;
    }

    //a 0 in the sequence array indicates no booking for that slot
    public static boolean[] isSlotBooked(int[] sequence) {
        boolean[] isSlotTaken = new boolean[sequence.length];
        for (int j : sequence) {
            if (j != 0) {
                isSlotTaken[j - 1] = true;
            }
        }
        return isSlotTaken;
    }
}
