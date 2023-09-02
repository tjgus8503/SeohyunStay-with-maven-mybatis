package seohyun.app.seohyunstay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    private String id;
    private String userId;
    private String username;
    private String phone;
    private Integer userNumber;
    private String roomId;
    private String roomName;
    private String hotelId;
    private String hotelName;
    private LocalDate reservedDate;
    private LocalDate checkoutDate;
    private String checkin;
    private String checkout;
    private Integer price;
}
