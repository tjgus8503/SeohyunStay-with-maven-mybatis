package seohyun.app.seohyunstay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private String id;
    private String userId;
    private String roomName;
    private String hotelId;
    // todo 가격은 1박 기준.
    private Long price;
    private Long count;
    private String content;
    private String imageUrl;
}
