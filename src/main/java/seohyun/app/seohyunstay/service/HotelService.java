package seohyun.app.seohyunstay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seohyun.app.seohyunstay.mapper.HotelMapper;
import seohyun.app.seohyunstay.model.Hotel;
import seohyun.app.seohyunstay.utils.ImageFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HotelService {
    private final HotelMapper hotelMapper;
    private final ImageFile imageFile;

    @Transactional
    public Map<String, String> CreateHotel(Hotel hotel, String userId, MultipartFile[] image) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            UUID uuid = UUID.randomUUID();
            hotel.setId(uuid.toString());
            hotel.setUserId(userId);
            if (image == null) {
                hotel.setImageUrl(null);
            } else {
                List<String> imageList = imageFile.CreateImage(image);
                String multiImages = String.join(",", imageList);
                hotel.setImageUrl(multiImages);
            }
            int result = hotelMapper.create(hotel);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 등록이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
