package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import seohyun.app.seohyunstay.model.Hotel;

@Mapper
public interface HotelMapper {

    @Insert("insert into hotel (id, hotelName, userId, address, content, imageUrl) " +
            "values (#{id}, #{hotelName}, #{userId}, #{address}, #{content}, #{imageUrl})")
    int create(Hotel hotel);
}
