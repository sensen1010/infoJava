package com.topnice.demoweb.token.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.topnice.demoweb.entity.Users;
import org.springframework.stereotype.Service;


/**
 * @author sensen
 * @date 2020-07-21 15:53
 */
@Service("TokenService")
public class TokenService {
    public String getToken(Users user) {
        String token = "";
        token = JWT.create().withAudience(user.getUserId())// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
