package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.Room;

@Mapper
public interface RoomMapper {

    @Insert("insert into room (id, userId, roomName, hotelId, price, count, content, imageUrl) " +
            "values (#{id}, #{userId}, #{roomName}, #{hotelId}, #{price}, #{count}, #{content}, #{imageUrl})")
    int create(Room room);

    @Select("select * from room where id = #{id}")
    Room findOneById(String id);

    @Update("update room set roomName = #{roomName}, price = #{price}, count = #{count}, content = #{content}," +
            "imageUrl = #{imageUrl} where id = #{id} and userId = #{userId}")
    int update(Room room);

    @Delete("delete from room where id = #{id}")
    int delete(String id);

    @Delete("delete from room where hotelId = #{hotelId}")
    void deleteByHotelId(String hotelId);
}
