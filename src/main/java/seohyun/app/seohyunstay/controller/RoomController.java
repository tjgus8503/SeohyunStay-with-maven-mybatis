package seohyun.app.seohyunstay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seohyun.app.seohyunstay.model.*;
import seohyun.app.seohyunstay.service.*;
import seohyun.app.seohyunstay.utils.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/room")
public class RoomController {
    private final RoomService roomService;
    private final UserService userService;
    private final HotelService hotelService;
    private final Jwt jwt;
    private final ImageFile imageFile;

    // 방 등록(소속된 호텔을 등록한 파트너만 방을 등록할 수 있다.)
    @PostMapping("/createroom")
    public ResponseEntity<Object> CreateRoom(
            @RequestHeader String authorization, @ModelAttribute Room room,
            @RequestPart(required = false) MultipartFile[] image
            ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            Hotel hotelInfo = hotelService.GetHotel(room.getHotelId());
            if (!hotelInfo.getUserId().equals(decoded)) {
                map.put("result", "failed 등록 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> create = roomService.CreateRoom(room, decoded, image);
            return new ResponseEntity<>(create, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 방 수정(방을 등록한 파트너 본인 또는 관리자(role=3)만 수정할 수 있다.)
    @PostMapping("/updateroom")
    public ResponseEntity<Object> UpdateRoom(
            @RequestHeader String authorization, @ModelAttribute Room room,
            @RequestPart(required = false) MultipartFile[] image
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            Room roomInfo = roomService.GetRoom(room.getId());
            User userInfo = userService.findUserId(decoded);
            if (!(roomInfo.getUserId().equals(decoded) || userInfo.getRole() == 3)) {
                map.put("result", "failed 수정 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> update = roomService.UpdateRoom(room, image);
            new Thread() {
                public void run() {
                    try{
                        imageFile.DeleteImage(roomInfo.getImageUrl());
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

    // 방 삭제(방을 등록한 파트너 본인 회또는 관리자(role=3)만 삭제할 수 있다.)
    @PostMapping("/deleteroom")
    public ResponseEntity<Object> DeleteRoom(
            @RequestHeader String authorization, @RequestParam String id
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            Room roomInfo = roomService.GetRoom(id);
            User userInfo = userService.findUserId(decoded);
            if (!(roomInfo.getUserId().equals(decoded) || userInfo.getRole() == 3)) {
                map.put("result", "failed 삭제 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> delete = roomService.DeleteRoom(id);
            new Thread() {
                public void run() {
                    try{
                        imageFile.DeleteImage(roomInfo.getImageUrl());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
            return new ResponseEntity<>(delete, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 방 상세페이지
    @GetMapping("/roomdetailpage")
    public ResponseEntity<Object> RoomDetailPage(@RequestParam String id) throws Exception {
        try{
            Room roomDetailPage = roomService.GetRoom(id);
            return new ResponseEntity<>(roomDetailPage, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
