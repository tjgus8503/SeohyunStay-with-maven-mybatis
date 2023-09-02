package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.Hotel;

import java.util.List;
import java.util.Map;

@Mapper
public interface HotelMapper {

    @Insert("insert into hotel (id, hotel_name, partner_id, address, content, image_url) " +
            "values (#{id}, #{hotelName}, #{partnerId}, #{address}, #{content}, #{imageUrl})")
    int create(Hotel hotel);

    @Select("select * from hotel where id = #{id}")
    Hotel findOneById(String id);

    @Update("update hotel set hotel_name = #{hotelName}, address = #{address}, content = #{content}, image_url = #{imageUrl} where id = #{id}")
    int update(Hotel hotel);

    @Delete("delete from hotel where id = #{id}")
    int delete(Hotel hotel);

    @Select("select * from hotel order by created_at desc limit 20 offset #{offset}")
    List<Hotel> findAll(Integer offset);

    @Select("select h.*, count(r.id) room_count from hotel h left join room r on h.id = r.hotel_id where h.id = #{id} group by h.id")
    Map<String, Object> getHotelWithRoomCount(String id);
}
