package seohyun.app.seohyunstay.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerAccount {
    private String id;
    private String userId;
    private String username;
    private String email;
    private String phone;
    private Integer role;
}
