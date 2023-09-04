package seohyun.app.seohyunstay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private String id;
    private String hotelName;
    private String partnerId;
    private String address;
    private String content;
    private String imageUrl;
}
