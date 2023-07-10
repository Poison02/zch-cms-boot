package cdu.zch.system.security;

import cdu.zch.system.common.constant.SecurityConstants;
import cdu.zch.system.security.userdetails.SysUserDetails;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zch
 * @date 2023/7/10
 **/
@Component
public class JwtTokenManager {

    /**
     * token加密密钥
     */
    @Value("${auth.token.secret_key}")
    private String secretKey;

    /**
     * token过期时间 单位：秒
     */
    @Value("${auth.token.ttl}")
    private Long tokenTtl;

    /**
     * secretKey
     */
    private byte[] secreteKeyBytes;

    private JwtParser jwtParser;

    @Resource
    private RedisTemplate redisTemplate;

    public String createToken(Authentication authentication) {
        // JWT声明
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        // 得到用户的信息
        SysUserDetails userDetails = (SysUserDetails) authentication.getPrincipal();
        // 将用户信息放入JWT声明中
        claims.put("jti", IdUtil.fastSimpleUUID());
        claims.put("userId", userDetails.getUserId());
        claims.put("username", userDetails.getUsername());
        claims.put("deptId", userDetails.getDeptId());
        claims.put("dataScope", userDetails.getDataScope());

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());
        claims.put("authorities", roles);

        // 权限数据存入Redis
        Set<String> perms = userDetails.getPerms();
        redisTemplate.opsForValue().set(SecurityConstants.USER_PERMS_CACHE_PREFIX + userDetails.getUserId(), perms);

        // 设置过期时间
        Date expirationTime = new Date(System.currentTimeMillis() + tokenTtl * 1000L);
        // 构建JWT并进行签名，使用compact()打包成一个JWT规范的字符串
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationTime)
                // 签名，密钥为：this.getSecretKeyBytes()，签名算法为：SignatureAlgorithm.HS256
                .signWith(Keys.hmacShaKeyFor(this.getSecretKeyBytes()), SignatureAlgorithm.HS256).compact();
    }

    /**
     * 获取认证信息
     * @param claims
     * @return
     */
    public Authentication getAuthentication(Claims claims) {
        SysUserDetails principal = new SysUserDetails();
        principal.setUserId(Convert.toLong(claims.get("userId")));
        principal.setUsername(Convert.toStr(claims.get("username")));
        principal.setDeptId(Convert.toLong(claims.get("deptId")));
        principal.setDataScope(Convert.toInt(claims.get("dataScope")));
        // SimpleGrantedAuthority 权限表示
        List<SimpleGrantedAuthority> authorities = ((ArrayList<String>) claims.get("authorities"))
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 解析并校验token
     * @param token
     * @return
     */
    public Claims parseAndValidateToken(String token) {
        Claims claims = this.getTokenClaims(token);
        // 判断该token是否在黑名单中
        Boolean isBlack = redisTemplate.hasKey(SecurityConstants.BLACK_TOKEN_CACHE_PREFIX + claims.get("jti"));

        if (Boolean.TRUE.equals(isBlack)) {
            throw new RuntimeException("token 已被禁用！");
        }
        return claims;
    }

    /**
     * 生成密钥
     * 在配置文件中配置的secret密钥使用Decoders.BASE64.decode()进行解码
     * @return
     */
    public byte[] getSecretKeyBytes() {
        if (secreteKeyBytes == null) {
            try {
                secreteKeyBytes = Decoders.BASE64.decode(secretKey);
            } catch (DecodingException e) {
                secreteKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            }
        }
        return secreteKeyBytes;
    }

    /**
     * 得到token的 claims
     * @param token
     * @return
     */
    public Claims getTokenClaims(String token) {
        if (jwtParser == null) {
            // 解析token并验证密钥
            jwtParser = Jwts.parserBuilder()
                    .setSigningKey(this.getSecretKeyBytes())
                    .build();
        }
        // 返回 claims 对象
        return jwtParser.parseClaimsJwt(token).getBody();
    }

}
