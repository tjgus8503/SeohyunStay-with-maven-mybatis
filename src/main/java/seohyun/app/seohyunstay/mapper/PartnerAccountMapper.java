package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import seohyun.app.seohyunstay.model.PartnerAccount;

import java.util.List;

@Mapper
public interface PartnerAccountMapper {

    @Insert("insert into partner_account (id, user_id, username, email, phone, role) values (#{id}, #{userId}, #{username}, #{email}, #{phone}, #{role})")
    int create(PartnerAccount partnerAccount);

    @Delete("delete from partner_account where user_id = #{userId}")
    int delete(PartnerAccount partnerAccount);

    @Select("select * from partner_account order by created_at desc limit 20 offset #{offset}")
    List<PartnerAccount> findAll(Integer offset);
}
