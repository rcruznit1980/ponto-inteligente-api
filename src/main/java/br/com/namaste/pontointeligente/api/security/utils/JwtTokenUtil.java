package br.com.namaste.pontointeligente.api.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author roalmeida.terceiro
 *
 */

@Component
public class JwtTokenUtil {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_CREATED = "created";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	// Obtem o username (email) contido no Token JWT
	public String getUsernameFromToken(String token) {
		String username;

		try {
			Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();

		} catch (Exception e) {
			username = null;
		}

		return username;
	}

	// Retorna data de expiração de um token JWT
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	// Cria um novo token
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = gerarToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}

		return refreshedToken;
	}

	// Verifica se um token JWT é valido
	public boolean tokenValido(String token) {
		return !tokenExpirado(token);

	}

	// Retorna um novo token JWT com base nos dados do usuário
	public String obterToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
		claims.put(CLAIM_KEY_CREATED, new Date());

		return gerarToken(claims);
	}

	// Realiza o parse do Token JWT para extrair as informações contidas no corpo
	private Claims getClaimsFromToken(String token) {
		Claims claims;

		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

//	Retorna a data de expiração com base na data atual
//	@param
//	@return token

	private Date gerarDataExpiracao() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	// Verifica se o token JWT está expirado
	private Boolean tokenExpirado(String token) {
		Date dataExpiracao = this.getExpirationDateFromToken(token);

		if (dataExpiracao == null) {
			return false;
		}
		return dataExpiracao.before(new Date());
	}

	private String gerarToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(gerarDataExpiracao())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

}
