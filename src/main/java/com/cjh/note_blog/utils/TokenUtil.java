package com.cjh.note_blog.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.handler.GlobalExceptionHandler;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtil.class);

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
	public static String getEmailFromToken(String token) throws MalformedJwtException, ExpiredJwtException {
		String username;
		try {
			username =getClaimsFromToken(token).getSubject();
		} catch (MalformedJwtException mje){
			throw mje;
		} catch (ExpiredJwtException e){
			throw e;
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
	private static Date getCreatedDateFromToken(String token) throws MalformedJwtException {
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
	 * 获取参数表
	 * @param token
	 * @return
	 */
	private static Claims getClaimsFromToken(String token) throws MalformedJwtException, ExpiredJwtException {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (MalformedJwtException e){
			// token 不合法 || token 过期
			LOGGER.error("token 不合法，"+e.getMessage());
			throw e;
		} catch (ExpiredJwtException e){
			LOGGER.error("token 过期，"+e.getMessage());
			throw e;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
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
	 * 生成token
	 * @param userDetails
	 * @return
	 */
	public static String generateToken(User userDetails) {
		Map<String, Object> claims = new HashMap<>(10);
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
	private static String generateToken(Map<String, Object> claims) {
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
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

		String email = getEmailFromToken(token);
		if (email != null){
			return Result.ok(email);
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
