package br.inpe.forum.config;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.inpe.forum.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	// EXPIRATION_TIME = 10 dias
	static final long EXPIRATION_TIME = 860_000_000;
	static final String SECRET = System.getenv("JWT_SECRET");
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletResponse response, String username) {
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();

		response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			try {
				Jws<Claims> jwt = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""));

				// Date validation
				if (jwt.getBody().getExpiration().before(new Date())) {
					throw new InvalidTokenException("\"message\":\"expired token\"");
				}

				// Algorithm validation
				if (!jwt.getHeader().getAlgorithm().equals("HS512"))
					throw new InvalidTokenException("\"message\":\"algorithm invalid\"");

				String user = jwt.getBody().getSubject();
				if (Objects.isNull(user))
					throw new InvalidTokenException("\"message\":\"subject invalid\"");

				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

			} catch (JwtException e) {
				throw new InvalidTokenException();
			}

		}
		return null;
	}

}