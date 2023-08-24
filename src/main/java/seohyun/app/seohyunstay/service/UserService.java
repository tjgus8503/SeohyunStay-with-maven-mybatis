package seohyun.app.seohyunstay.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seohyun.app.seohyunstay.mapper.*;
import seohyun.app.seohyunstay.model.*;
import seohyun.app.seohyunstay.utils.Bcrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserMapper userMapper;
    private final PartnerReqMapper partnerReqMapper;
    private final Bcrypt bcrypt;

    @Transactional
    public Map<String, String> SignUp(User user) throws Exception {
        try {
            Map<String, String> map = new HashMap<>();
            UUID uuid = UUID.randomUUID();
            user.setId(uuid.toString());
            String hashPassword = bcrypt.HashPassword(user.getPassword());
            user.setPassword(hashPassword);
            int result = userMapper.create(user);
            if (result == 0) {
                throw new Exception("failed.");
            }
            map.put("result", "success 등록이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public void CheckUserId(String userId) throws Exception {
        try{
            User checkUserId = userMapper.findOneByUserId(userId);
            if (checkUserId != null) {
                throw new Exception("failed. 이미 등록되어있는 아이디입니다.");
            }
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public User findUserId(String userId) throws Exception {
        try {
            User findUserId = userMapper.findOneByUserId(userId);
            if (findUserId == null) {
                throw new Exception("failed. 일치하는 정보가 없습니다.");
            }
            return findUserId;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> UpdateUser(User user, String userId) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            user.setUserId(userId);
            int result = userMapper.update(user);
            if (result == 0) {
                throw new Exception("failed.");
            }
            map.put("result", "success 수정이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> UnRegister(User user, String userId) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            user.setUserId(userId);
            int result = userMapper.delete(user);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 탈퇴가 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> UpdatePassword(User user) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            int result = userMapper.updatePassword(user);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 변경이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> CreatePartnerReq(String userId) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            PartnerReq partnerReq = new PartnerReq();
            UUID uuid = UUID.randomUUID();
            partnerReq.setId(uuid.toString());
            partnerReq.setUserId(userId);
            int result = partnerReqMapper.create(partnerReq);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 등록이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public Map<String, String> AcceptPartner(User user) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            int result = userMapper.updateRole(user);
            if (result == 0) {
                throw new Exception("failed");
            }
            map.put("result", "success 수락이 완료되었습니다.");
            return map;
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    @Transactional
    public void DeletePartnerReq(PartnerReq partnerReq) throws Exception {
        try{
            int result = partnerReqMapper.delete(partnerReq);
            if (result == 0) {
                throw new Exception("failed");
            }
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    public List<PartnerReq> GetAllPartnerReq() throws Exception {
        try{
            return partnerReqMapper.findAll();
        } catch (Exception e){
            throw new Exception(e);
        }
    }

}
