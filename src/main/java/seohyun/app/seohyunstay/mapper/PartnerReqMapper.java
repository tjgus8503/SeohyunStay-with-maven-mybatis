package seohyun.app.seohyunstay.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import seohyun.app.seohyunstay.model.PartnerReq;

import java.util.List;

@Mapper
public interface PartnerReqMapper {

    @Insert("insert into partnerReq (id, userId) values (#{id}, #{userId})")
    int create(PartnerReq partnerReq);

    @Delete("delete from partnerReq where id = #{id} and userId = #{userId}")
    int delete(PartnerReq partnerReq);

    @Select("select * from partnerReq")
    List<PartnerReq> findAll();
}
