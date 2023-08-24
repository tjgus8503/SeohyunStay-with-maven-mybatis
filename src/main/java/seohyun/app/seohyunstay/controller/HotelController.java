package seohyun.app.seohyunstay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seohyun.app.seohyunstay.model.Hotel;
import seohyun.app.seohyunstay.model.User;
import seohyun.app.seohyunstay.service.*;
import seohyun.app.seohyunstay.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/hotel")
public class HotelController {
    private final HotelService hotelService;
    private final UserService userService;
    private final Jwt jwt;
    private final ImageFile imageFile;

    // 호텔 등록(파트너)
    @PostMapping("/createhotel")
    public ResponseEntity<Object> CreateHotel(
            @RequestHeader String authorization, @ModelAttribute Hotel hotel,
            @RequestPart(required = false) MultipartFile[] image
            ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            if (userInfo.getRole() != 2) {
                map.put("result", "failed 등록 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> create = hotelService.CreateHotel(hotel, decoded, image);
            return new ResponseEntity<>(create, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 호텔 수정(파트너, 관리자)
}
