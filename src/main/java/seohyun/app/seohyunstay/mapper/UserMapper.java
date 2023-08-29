package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.PartnerAccount;
import seohyun.app.seohyunstay.model.User;

@Mapper
public interface UserMapper {
    @Insert("insert into user (id, userId, password, username, email, phone, role)" +
            "values (#{id}, #{userId}, #{password}, #{username}, #{email}, #{phone}, #{role})")
    int create(User user);

    @Select("select * from user where userId = #{userId}")
    User findOneByUserId(String userId);

    @Update("update user set username = #{username}, email = #{email}, phone = #{phone} where userId = #{userId}")
    int update(User user);

    @Delete("delete from user where userId = #{userId}")
    int delete(User user);

    @Update("update user set password = #{password} where userId = #{userId}")
    int updatePassword(User user);

    @Update("update user set role = 2 where userId = #{userId}")
    int updateRole(PartnerAccount partnerAccount);
}
