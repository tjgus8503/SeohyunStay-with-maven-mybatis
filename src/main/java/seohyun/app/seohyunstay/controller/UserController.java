package seohyun.app.seohyunstay.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seohyun.app.seohyunstay.model.User;
import seohyun.app.seohyunstay.service.UserService;
import seohyun.app.seohyunstay.utils.*;

import java.util.HashMap;
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
            map.put("result", "success 로그인 성공.");
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
    // 비밀번호 확인 후 탈퇴 완료.
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
            User user = new User();
            String hashPassword = bcrypt.HashPassword(req.get("newPassword"));
            user.setId(userInfo.getId());
            user.setUserId(userInfo.getUserId());
            user.setPassword(hashPassword);
            user.setUsername(userInfo.getUsername());
            user.setEmail(userInfo.getEmail());
            user.setPhone(userInfo.getPhone());
            user.setRole(userInfo.getRole());
            Map<String, String> updatePassword = userService.UpdatePassword(user);
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
            Map<String, String> map = new HashMap<>();
            String decoded = jwt.VerifyToken(authorization);
            User userInfo = userService.findUserId(decoded);
            return new ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (Exception e){
            Map<String, String> map = new HashMap<>();
            map.put("error", e.toString());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }
}
