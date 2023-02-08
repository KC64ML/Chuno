package com.leesfamily.chuno.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.leesfamily.chuno.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoLoginController {
    // ======== 카카오 로그인
    @Value("${secret.key}")
    String secretKey;

    final private UserService userService;

    @PostMapping("/getTokken")
    public ResponseEntity<Map<String, Object>> getToken(@RequestBody String code) {
        System.out.println("컨트롤러에서 받은 코드 : " + code);
        String access_Token = "";
        String refresh_Token = "";//
        String reqURL = "https://kauth.kakao.com/oauth/token";
        Map<String, Object> res = new HashMap<>();
        HttpStatus httpStatus = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=9733352823239497d6928853e1e59954"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=https://i8d208.p.ssafy.io/api/oauth.html"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            String ctnt = conn.getResponseMessage();
            System.out.println("성공여부 : " + responseCode);
            System.out.println("내용 : " + ctnt);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject jsonObject = element.getAsJsonObject();

            access_Token = jsonObject.get("access_token").getAsString();
            refresh_Token = jsonObject.get("refresh_token").getAsString();

            System.out.println("토큰 : " + access_Token);

            br.close();
            bw.close();

            String email = getInfo(access_Token);

            System.out.println(email);

            if (email.equals("no_email")) {
                res.put("code", "no_email");
                httpStatus = HttpStatus.OK;
            }
            System.out.println("여기까지");

            //이메일을 데이터베이스에서 뒤져요
            UserEntity user = userService.findUserByEmail(email);
            //이메일이 있으면 이미 가입한 유저에요 JWT토큰을 만들어 홈으로 userDto와 함께 가요
            //이메일이 없으면 가입한 적이 없는 유저에요
            if (user == null) {
                res.put("code", "no_email");
                res.put("result", email);
                httpStatus = HttpStatus.OK;
            } else {
                res.put("code", "member");
                res.put("result", makeToken(user.getId()));
                httpStatus = HttpStatus.OK;
            }
        } catch (Exception e) {
            res.put("code", e.getMessage());
            httpStatus = HttpStatus.EXPECTATION_FAILED;
        }
        return new ResponseEntity<>(res, httpStatus);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> getAccessToken(@RequestBody String code) {
        System.out.println("컨트롤러에서 받은 코드 : " + code);
        String access_Token = "";
        String refresh_Token = "";//
        String reqURL = "https://kauth.kakao.com/oauth/token";
        Map<String, Object> res = new HashMap<>();
        HttpStatus httpStatus = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=9733352823239497d6928853e1e59954"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=https://i8d208.p.ssafy.io/oauth"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            String ctnt = conn.getResponseMessage();
            System.out.println("성공여부 : " + responseCode);
            System.out.println("내용 : " + ctnt);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            JsonObject jsonObject = element.getAsJsonObject();

            access_Token = jsonObject.get("access_token").getAsString();
            refresh_Token = jsonObject.get("refresh_token").getAsString();

            System.out.println("토큰 : " + access_Token);

            br.close();
            bw.close();

            String email = getInfo(access_Token);

            System.out.println(email);

            if (email.equals("no_email")) {
                res.put("code", "no_email");
                httpStatus = HttpStatus.OK;
            }
            System.out.println("여기까지");

            //이메일을 데이터베이스에서 뒤져요
            UserEntity user = userService.findUserByEmail(email);
            //이메일이 있으면 이미 가입한 유저에요 JWT토큰을 만들어 홈으로 userDto와 함께 가요
            //이메일이 없으면 가입한 적이 없는 유저에요
            if (user == null) {
                res.put("code", "no_email");
                res.put("result", email);
                httpStatus = HttpStatus.OK;
            } else {
                res.put("code", "member");
                res.put("result", makeToken(user.getId()));
                httpStatus = HttpStatus.OK;
            }
        } catch (Exception e) {
            res.put("code", e.getMessage());
            httpStatus = HttpStatus.EXPECTATION_FAILED;
        }
        return new ResponseEntity<>(res, httpStatus);
    }

    @PostMapping("/mobile/login")
    ResponseEntity<Map<String, Object>> getAccessTokenFromMobile(@RequestBody String access_Token) {
//    	String access_Token = map.get("key");
        System.out.println("토큰 : " + access_Token);
        Map<String, Object> res = new HashMap<>();
        HttpStatus httpStatus = null;
        String email;
        try {
            email = getInfo(access_Token);
            System.out.println(email);

            if (email.equals("no_email")) {
                res.put("code", "no_email");
                httpStatus = HttpStatus.OK;
            }
            System.out.println("여기까지");

            //이메일을 데이터베이스에서 뒤져요
            UserEntity user = userService.findUserByEmail(email);
            //이메일이 있으면 이미 가입한 유저에요 JWT토큰을 만들어 홈으로 userDto와 함께 가요
            //이메일이 없으면 가입한 적이 없는 유저에요
            if (user == null) {
                res.put("code", "no_email");
                res.put("result", email);
                httpStatus = HttpStatus.OK;
            } else {
                res.put("code", "member");
                res.put("result", makeToken(user.getId()));
                httpStatus = HttpStatus.OK;
            }
        } catch (Exception e) {
            res.put("code", e.getMessage());
            httpStatus = HttpStatus.EXPECTATION_FAILED;
        }
        return new ResponseEntity<>(res, httpStatus);
    }

    String makeToken(Long user_id) {
        System.out.println("여기도 잘되는데요");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 86400000);
        System.out.println("여기도 되나요??");
        System.out.println(secretKey);
        return Jwts.builder()
                .setSubject("access_key") // 열쇠? 키?
                .setIssuedAt(new Date()) // 발행일
                .setExpiration(expiryDate) // 만료일
                //만약 claim을 넣고 싶다면
				.claim("user_id", user_id) // 넣을 payload 키
                .signWith(SignatureAlgorithm.HS512, secretKey) // 암호화 방식
                .compact(); // 묶어
    }

    String getInfo(String token) throws IOException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        URL url;
        try {
            url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if(hasEmail){
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("id : " + id);
            System.out.println("email : " + email);
            return email;
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "no_email";
        }
    }


    @PostMapping(name = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    String register(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "nickname") String nickname,
            @RequestPart(value = "email") String email
    ) {
        UserEntity user = UserEntity.builder()
                .nickname(nickname)
                .email(email)
                .build();
        Long userId = userService.register(user, file);
        return makeToken(userId);
    }

    @PostMapping("/tokenConfirm")
    public int tokenConfirm(@RequestBody String token) {
        try {
            Claims claim = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            System.out.println(claim.get("user_id"));
            return 1;
        } catch(Exception e) {
            return 0;
        }
        //만약 claim을 알고 싶다면
        //Claim claim = Jwts.parser().setSigningKey(@Value(${secret.key})).parseClaimsJws(token).getBody();
        //System.out.println(claim.getUser());
    }
}
