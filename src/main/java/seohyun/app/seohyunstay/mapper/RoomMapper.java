package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.Reservation;
import seohyun.app.seohyunstay.model.Room;

@Mapper
public interface RoomMapper {

    @Insert("insert into room (id, room_name, hotel_id, price, count, content, image_url) " +
            "values (#{id}, #{roomName}, #{hotelId}, #{price}, #{count}, #{content}, #{imageUrl})")
    int create(Room room);

    @Select("select * from room where id = #{id}")
    Room findOneById(String id);

    @Update("update room set room_name = #{roomName}, price = #{price}, count = #{count}, content = #{content}," +
            "image_url = #{imageUrl} where id = #{id}")
    int update(Room room);

    @Delete("delete from room where id = #{id}")
    int delete(Room room);

    @Delete("delete from room where hotelId = #{hotelId}")
    void deleteByHotelId(String hotelId);

    @Update("update room set count = count - 1 where id = #{roomId} and count >= 1")
    int updateCount(Reservation reservation);

    @Update("update room set count = count + 1 where id = #{roomId}")
    int addCount(Reservation reservationInfo);
}
