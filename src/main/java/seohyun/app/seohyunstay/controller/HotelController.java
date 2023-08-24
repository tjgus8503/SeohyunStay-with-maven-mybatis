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
    private final RoomService roomService;
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

    // 호텔 수정(등록한 본인(파트너) or 관리자)
    @PostMapping("/updatehotel")
    public ResponseEntity<Object> UpdateHotel(
            @RequestHeader String authorization, @ModelAttribute Hotel hotel,
            @RequestPart(required = false) MultipartFile[] image
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            Hotel hotelInfo = hotelService.GetHotel(hotel.getId());
            User userInfo = userService.findUserId(decoded);
            if (!(hotelInfo.getUserId().equals(decoded) || userInfo.getRole() == 3)) {
                map.put("result", "failed 수정 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> update = hotelService.UpdateHotel(hotel, image);
            new Thread() {
                public void run() {
                    try{
                        imageFile.DeleteImage(hotelInfo.getImageUrl());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
            return new ResponseEntity<>(update, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 호텔 삭제 (등록한 본인(파트너) or 관리자)
    @PostMapping("/deletehotel")
    public ResponseEntity<Object> DeleteHotel(
            @RequestHeader String authorization, @RequestParam String id
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            Hotel hotelInfo = hotelService.GetHotel(id);
            User userInfo = userService.findUserId(decoded);
            if (!(hotelInfo.getUserId().equals(decoded) || userInfo.getRole() == 3)) {
                map.put("result", "failed 삭제 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> delete = hotelService.DeleteHotel(id);
            new Thread() {
                public void run() {
                    try{
                        imageFile.DeleteImage(hotelInfo.getImageUrl());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
            // 호텔 삭제 시 호텔 소속 방들도 모두 삭제.
            roomService.DeleteRoomByHotelId(id);
            return new ResponseEntity<>(delete, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 호텔 전체 조회
    @GetMapping("/getallhotel")
    public ResponseEntity<Object> GetAllHotel() throws Exception {
        try{
            List<Hotel> hotelList = hotelService.GetAllHotel();
            return new ResponseEntity<>(hotelList, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 호텔 상세 조회
}
