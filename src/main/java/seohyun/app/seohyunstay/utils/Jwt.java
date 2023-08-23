package seohyun.app.seohyunstay.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class Jwt {
    @Value("${ACCESS_KEY}")
    String AccessKey;

    private final Long expiredTime = 1000 * 60L * 60L * 24L; // 1일

    ////////////////// create //////////////////
    public String CreateToken(String username) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(username)
                .setHeader(createHeader())
                .setClaims(createClaims(username))
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(SignatureAlgorithm.HS256, AccessKey)
                .compact();
    }

//    public String CreateRToken(String userid) {
//        Date now = new Date();
//        Map<String, Object> payloads = new HashMap<>();
//        payloads.put("userid", userid);
//
//        return Jwts.builder()
//                .setSubject("rauthorization")
//                .setHeader(createHeader())
//                .setClaims(payloads)
//                .setExpiration(new Date(now.getTime() + rexpiredTime))
//                .signWith(SignatureAlgorithm.HS256, RefreshKey)
//                .compact();
//    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return claims;
    }

    ////////////////// decoded //////////////////

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(AccessKey).parseClaimsJws(token).getBody();
    }

    public String VerifyToken(String token) {
        return (String) getClaims(token).get("username");
    }

//    private Claims getRClaims(String token) {
//        return Jwts.parser().setSigningKey(RefreshKey).parseClaimsJws(token).getBody();
//    }

//    public String VerifyRToken(String token) {
//        return (String) getRClaims(token).get("userid");
//    }
}
