package seohyun.app.seohyunstay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seohyun.app.seohyunstay.model.PartnerAccount;
import seohyun.app.seohyunstay.model.User;
import seohyun.app.seohyunstay.service.UserService;
import seohyun.app.seohyunstay.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final Bcrypt bcrypt;
    private final Jwt jwt;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> SignUp(@RequestBody User user) throws Exception {
        try{
            userService.CheckUserId(user.getUserId());
            Map<String, String> create = userService.SignUp(user);
            return new ResponseEntity<>(create, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<Object> SignIn(@RequestBody User user) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            User userinfo = userService.findUserId(user.getUserId());
            Boolean compare = bcrypt.CompareHash(user.getPassword(), userinfo.getPassword());
            if (compare == false) {
                map.put("result", "failed 비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            String authorization = jwt.CreateToken(user.getUserId());
            map.put("authorization", authorization);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 회원정보 수정
    @PostMapping("/updateuser")
    public ResponseEntity<Object> UpdateUser(
            @RequestHeader String authorization, @RequestBody User user
    ) throws Exception {
        try{
            String decoded = jwt.VerifyToken(authorization);
            Map<String, String> update = userService.UpdateUser(user, decoded);
            return new ResponseEntity<>(update, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 회원탈퇴
    // 비밀번호 한번더 확인 후 탈퇴 완료.
    @PostMapping("/unregister")
    public ResponseEntity<Object> UnRegister(
            @RequestHeader String authorization, @RequestBody User user
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            Boolean compare = bcrypt.CompareHash(user.getPassword(), userInfo.getPassword());
            if (compare == false) {
                map.put("result", "failed 비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> delete = userService.UnRegister(user, decoded);
            return new ResponseEntity<>(delete, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 비밀번호 변경
    @PostMapping("/updatepassword")
    public ResponseEntity<Object> UpdatePassword(
            @RequestHeader String authorization, @RequestBody Map<String, String> req
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            Boolean compare = bcrypt.CompareHash(req.get("password"), userInfo.getPassword());
            if (compare == false) {
                map.put("result", "failed 비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }

            String hashPassword = bcrypt.HashPassword(req.get("newPassword"));
            userInfo.setPassword(hashPassword);
            Map<String, String> updatePassword = userService.UpdatePassword(userInfo);
            return new ResponseEntity<>(updatePassword, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<Object> MyPage(
            @RequestHeader String authorization
    ) throws Exception {
        try{
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 파트너 등록 신청(일반 유저(role=1)만 신청할 수 있다.)
    @PostMapping("/createpartneraccount")
    public ResponseEntity<Object> CreatePartnerAccount(
            @RequestHeader String authorization
            ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            if (userInfo.getRole() != 1) {
                map.put("result", "failed 신청 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> createPA = userService.CreatePartnerAccount(userInfo);
            return new ResponseEntity<>(createPA, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 파트너 신청 수락(관리자(role=3)만 수락할 수 있다.)
    @PostMapping("/acceptpartner")
    public ResponseEntity<Object> AcceptPartner(
            @RequestHeader String authorization, @RequestBody PartnerAccount partnerAccount
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User findUserId = userService.findUserId(decoded);
            if (findUserId.getRole() != 3) {
                map.put("result", "failed 수락 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Map<String, String> updateRole = userService.AcceptPartner(partnerAccount);
            // 수락 완료 시, 파트너 신청 목록에서 해당 신청 건은 삭제.
            userService.DeletePartnerAccount(partnerAccount);
            return new ResponseEntity<>(updateRole, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    // 파트너 신청목록 조회(관리자(role=3)만 조회할 수 있다.)
    @GetMapping("/getallpartneraccount")
    public ResponseEntity<Object> GetAllPartnerAccount(
            @RequestHeader String authorization, @RequestParam Integer page
    ) throws Exception {
        try{
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            if (userInfo.getRole() != 3) {
                map.put("result", "failed 조회 권한이 없습니다.");
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            Integer offset = 0;
            if (page > 1) {
                offset = 20 * (page - 1);
            }
            List<PartnerAccount> partnerAccountList = userService.GetAllPartnerAccount(offset);
            return new ResponseEntity<>(partnerAccountList, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
