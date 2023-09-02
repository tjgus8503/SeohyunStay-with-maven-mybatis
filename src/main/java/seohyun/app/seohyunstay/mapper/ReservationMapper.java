package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.Reservation;

import java.util.List;

@Mapper
public interface ReservationMapper {

    @Insert("insert into reservation (id, user_id, username, phone, user_number, room_id, room_name, hotel_id, hotel_name, reserved_date, checkout_date, price) " +
            "values (#{id}, #{userId}, #{username}, #{phone}, #{userNumber}, #{roomId}, #{roomName}, #{hotelId}, #{hotelName}, #{reservedDate}, #{checkoutDate}, #{price})")
    int create(Reservation reservation);

    @Delete("delete from reservation where id = #{id} and user_id = #{userId}")
    int delete(String id, String userId);

    @Select("select * from reservation where id = #{id}")
    Reservation findOneById(String id);

    @Select("select * from reservation where user_id = #{userId} order by created_at desc limit 20 offset #{offset}")
    List<Reservation> findByUserId(String userId, Integer offset);

    @Select("select * from reservation where hotel_id = #{hotelId} order by created_at desc limit 20 offset #{offset}")
    List<Reservation> findByHotelId(String hotelId, Integer offset);

    @Update("update reservation set checkin = #{checkin} where id = #{id}")
    int checkin(Reservation reservation);

    @Update("update reservation set checkout = #{checkout} where id = #{id}")
    int checkout(Reservation reservation);
}
