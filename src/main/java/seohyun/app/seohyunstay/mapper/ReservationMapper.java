package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.Reservation;

import java.util.List;

@Mapper
public interface ReservationMapper {

    @Insert("insert into reservation (id, userId, roomId, hotelId, startedAt, endedAt, count, checkin, checkout) " +
            "values (#{id}, #{userId}, #{roomId}, #{hotelId}, #{startedAt}, #{endedAt}, #{count}, #{checkin}, #{checkout})")
    int create(Reservation reservation);

    @Delete("delete from reservation where id = #{id} and userId = #{userId}")
    int delete(String id, String userId);

    @Select("select * from reservation where id = #{id}")
    Reservation findOneById(String id);

    @Select("select * from reservation where userId = #{userId} order by createdAt desc limit 20 offset #{offset}")
    List<Reservation> findByUserId(String userId, Integer offset);

    @Select("select * from reservation where hotelId = #{hotelId} order by createdAt desc limit 20 offset #{offset}")
    List<Reservation> findByHotelId(String hotelId, Integer offset);

    @Update("update reservation set checkin = now() where id = #{id} and userId = #{userId}")
    int checkin(String id, String userId);

    @Update("update reservation set checkout = now() where id = #{id} and userId = #{userId}")
    int checkout(String id, String userId);
}
