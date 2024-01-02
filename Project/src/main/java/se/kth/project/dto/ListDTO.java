package se.kth.project.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ListDTO {
    private Integer id;
    private Integer userId;
    @NotEmpty(message = "Please select which course this reservation list is for.")
    private Integer courseId;
    @NotEmpty(message = "You need to provide a description over what kind of booking list this is.")
    private String description;
    @NotEmpty(message = "Specify the location if on campus, or zoom if online.")
    private String location;
    @Future(message = "The date and time must be in the future.")
    private LocalDateTime start;
    @Positive(message = "Please specify the allotted time for each booking.")
    private int intervall;
    @Positive(message = "Specify the maximum allowed number of bookings for this reservation.")
    private int maxSlots;
}
