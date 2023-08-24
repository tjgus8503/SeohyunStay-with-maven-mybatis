package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.Hotel;

import java.util.List;

@Mapper
public interface HotelMapper {

    @Insert("insert into hotel (id, hotelName, userId, address, content, imageUrl) " +
            "values (#{id}, #{hotelName}, #{userId}, #{address}, #{content}, #{imageUrl})")
    int create(Hotel hotel);

    @Select("select * from hotel where id = #{id}")
    Hotel findOneById(String id);

    @Update("update hotel set hotelName = #{hotelName}, address = #{address}, content = #{content}, imageUrl = #{imageUrl} where id = #{id} and userId = #{userId}")
    int update(Hotel hotel);

    @Delete("delete from hotel where id = #{id}")
    int delete(String id);

    @Select("select * from hotel")
    List<Hotel> findAll();
}
