package seohyun.app.seohyunstay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private String id;
    private String roomName;
    private String hotelId;
    private Integer price;
    private Integer count;
    private String content;
    private String imageUrl;
}
