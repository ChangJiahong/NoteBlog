package com.cjh.note_blog.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {


	private static final String CLAIM_KEY_Email = "sub";
	private static final String CLAIM_KEY_ID = "id";
	private static final String CLAIM_KEY_CREATED = "created";
	private static final String CLAIM_KEY_ROLES = "roles";

	/**
	 * 秘钥
	 * Base64加密后的秘钥//2327085154
	 */
	private static String secret ;//= "MjMyNzA4NTE1NA==";

	/**
	 * 有效期 ，过期时长，单位为秒,可以通过配置写入。
	 */
	private static int expiration ;//= 7*24*60*60;

	@Value("${jwt.token.secret:MjMyNzA4NTE1NA==}")
	public void setSecret(String secret) {
		TokenUtil.secret = secret;
	}

	@Value("#{${jwt.token.expiration:7*24*60*60}}")
	public void setExpiration(int expiration) {
		TokenUtil.expiration = expiration;
	}


	/**
	 * 获取Email
	 * @param token
	 * @return
	 */
	public static String getEmailFromToken(String token) throws MalformedJwtException {
		String username;
		try {
			username =getClaimsFromToken(token).getSubject();
		} catch (MalformedJwtException mje){
			throw mje;
		} catch (Exception e) {
			username = null;
		}
		return username;
	}


	/**
	 * 获取创建时间
	 * @param token
	 * @return
	 */
	public static Date getCreatedDateFromToken(String token) throws MalformedJwtException {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (MalformedJwtException mje){
			throw mje;
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	/**
	 * 获取有效期
	 * @param token
	 * @return
	 */
	public static Date getExpirationDateFromToken(String token) throws MalformedJwtException {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (MalformedJwtException mje){
			throw mje;
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	/**
	 * 获取参数表
	 * @param token
	 * @return
	 */
	public static Claims getClaimsFromToken(String token) throws MalformedJwtException {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (MalformedJwtException mje){
			throw mje;
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	/**
	 * 生成有效期
	 * @return
	 */
	private static Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * token是否过期
	 * 过期 return true
	 * 有效 return false
	 * @param token
	 * @return
	 */
	public static Boolean isTokenExpired(String token) throws MalformedJwtException {

		final Date expiration = getExpirationDateFromToken(token);

		return expiration.before(new Date());
	}

	/**
	 * 生成token
	 * @param userDetails
	 * @return
	 */
	public static String generateToken(User userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_Email, userDetails.getEmail());
		claims.put(CLAIM_KEY_CREATED, new Date());
		claims.put(CLAIM_KEY_ID, userDetails.getUid());
		claims.put(CLAIM_KEY_ROLES, userDetails.getRoles());
		return generateToken(claims);
	}


	/**
	 * 生成token
	 * @param claims
	 * @return
	 */
	public static String generateToken(Map<String, Object> claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}



	public static Boolean canTokenBeRefreshed(String token) {
		return !isTokenExpired(token);
	}

	/**
	 * 刷新token
	 * @param token
	 * @return
	 */
	public static String refreshToken(String token) throws MalformedJwtException {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (MalformedJwtException mje){
			throw mje;
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	/**
	 * 验证token
	 * @param token
	 * @return
	 */
	public static Boolean validateToken(String token) {
		final String email = getEmailFromToken(token);
		return (email != null
						&& !isTokenExpired(token));
	}


	/**
	 * 检查token
	 * 成功则返回 User
	 * @param token
	 * @return
	 */
	public static Result checkToken(String token){
		if (StringUtils.isBlank(token)){
			// token为空
			return Result.fail(StatusCode.TokenIsEmpty);
		}
		try {
			if (isTokenExpired(token)){
				// error: token过期
				return Result.fail(StatusCode.TokenExpired);
			}
			String email = getEmailFromToken(token);
			if (email != null){
				return Result.ok(email);
			}
		}catch (MalformedJwtException e){
			// error: token不合法
			return Result.fail(StatusCode.TokenIsNotValid);
		}

		// error: token不合法
		return Result.fail(StatusCode.TokenIsNotValid);
	}

	public static String getSecret() {
		return secret;
	}

	public static int getExpiration() {
		return expiration;
	}

}
