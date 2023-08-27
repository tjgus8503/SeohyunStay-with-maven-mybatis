package seohyun.app.seohyunstay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private String id;
    private String userId;
    private String roomId;
    private String hotelId;
    private Long count;
    private LocalDateTime checkin;
    private LocalDateTime checkout;
}
