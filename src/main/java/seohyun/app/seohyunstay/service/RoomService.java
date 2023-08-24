package seohyun.app.seohyunstay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seohyun.app.seohyunstay.mapper.RoomMapper;
import seohyun.app.seohyunstay.model.Room;
import seohyun.app.seohyunstay.utils.ImageFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomMapper roomMapper;
    private final ImageFile imageFile;

    @Transactional
    public Map<String, String> CreateRoom(Room room, String userId, MultipartFile[] image) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            UUID uuid = UUID.randomUUID();
            room.setId(uuid.toString());
            room.setUserId(userId);
            if (image == null) {
                room.setImageUrl(null);
            } else {
                List<String> imageList = imageFile.CreateImage(image);
                String multiImages = String.join(",", imageList);
                room.setImageUrl(multiImages);
            }
            int result = roomMapper.create(room);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 등록이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public Room GetRoom(String id) throws Exception {
        try{
            return roomMapper.findOneById(id);
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> UpdateRoom(Room room, MultipartFile[] image) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            if (image == null) {
                room.setImageUrl(null);
            } else {
                List<String> imageList = imageFile.CreateImage(image);
                String multiImages = String.join(",", imageList);
                room.setImageUrl(multiImages);
            }
            int result = roomMapper.update(room);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 수정이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> DeleteRoom(String id) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            int result = roomMapper.delete(id);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 삭제가 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void DeleteRoomByHotelId(String hotelId) throws Exception {
        try{
            roomMapper.deleteByHotelId(hotelId);
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
