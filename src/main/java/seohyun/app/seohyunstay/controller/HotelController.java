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

    // 호텔 등록(파트너(role=2)만 호텔을 등록할 수 있다.)
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

    // 호텔 수정(호텔을 등록한 파트너 본인 또는 관리자(role=3)만 수정할 수 있다.)
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

    // 호텔 삭제(호텔을 등록한 파트너 본인 또는 관리자(role=3)만 삭제할 수 있다.)
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
            // 호텔 삭제 시, 해당 호텔에 소속된 방들도 모두 삭제.
            roomService.DeleteRoomByHotelId(id);
            return new ResponseEntity<>(delete, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 호텔 리스트 전체 조회
    @GetMapping("/getallhotel")
    public ResponseEntity<Object> GetAllHotel(
            @RequestParam Integer pageNumber
    ) throws Exception {
        try{
            Integer offset = 0;
            if (pageNumber > 1) {
                offset = 20 * (pageNumber - 1);
            }
            List<Hotel> hotelList = hotelService.GetAllHotel(offset);
            return new ResponseEntity<>(hotelList, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 호텔 상세페이지 (with 방 개수)
    @GetMapping("/hoteldetailpage")
    public ResponseEntity<Object> HotelDetailPage(@RequestParam String id) throws Exception {
        try{
            Map<String, Object> hotelWithRoomCount = hotelService.HotelDetailPage(id);
            return new ResponseEntity<>(hotelWithRoomCount, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
