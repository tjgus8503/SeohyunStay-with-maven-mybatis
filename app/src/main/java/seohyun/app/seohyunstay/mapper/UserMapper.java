package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.*;
import seohyun.app.seohyunstay.model.PartnerAccount;
import seohyun.app.seohyunstay.model.User;

@Mapper
public interface UserMapper {
    @Insert("insert into user (id, user_id, password, username, email, phone, role)" +
            "values (#{id}, #{userId}, #{password}, #{username}, #{email}, #{phone}, #{role})")
    int create(User user);

    @Select("select * from user where user_id = #{userId}")
    User findOneByUserId(String userId);

    @Update("update user set username = #{username}, email = #{email}, phone = #{phone} where user_id = #{userId}")
    int update(User user);

    @Delete("delete from user where user_id = #{userId}")
    int delete(User user);

    @Update("update user set password = #{password} where user_id = #{userId}")
    int updatePassword(User user);

    @Update("update user set role = 2 where user_id = #{userId}")
    int updateRole(PartnerAccount partnerAccount);
}
