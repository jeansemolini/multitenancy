package com.concept.token;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.concept.constants.AppConstants.TOKEN;
import com.concept.security.UsuarioSistema;
import com.concept.util.JDBCUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	static void addAuthentication(HttpServletResponse response, Authentication auth) {

		UsuarioSistema usuario = (UsuarioSistema) auth.getPrincipal();;

		String sql = "SELECT razao_social from `empresas_master`.`empresa` WHERE cnpj = '" + usuario.getUsuario().getEmpresa() + "';";

		String razaoSocial = JDBCUtil.consultaBanco(usuario.getUsuario().getEmpresa(), sql);

		if (!usuario.getUsuario().isAtivo()) {
			response.addHeader("retorno", "Usuario inativo");
		} else {
			String JWT = Jwts.builder()
					.claim("login", usuario.getUsername())
					.claim("empresa", razaoSocial)
					.claim("nome", usuario.getUsuario().getNome())
					.signWith(SignatureAlgorithm.HS512, TOKEN.SECRET.getBytes())
					.compact();

			response.addHeader(TOKEN.HEADER_STRING, TOKEN.PREFIX + " " + JWT);
		}
	}

	static void addFailedAuthentication(HttpServletResponse response, AuthenticationException failed) {
		response.addHeader("retorno", "Usuario e/ou senha invalidos");
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(TOKEN.HEADER_STRING);

		if (token != null) {
			try {
				Claims clains = Jwts.parser()
						.setSigningKey(TOKEN.SECRET.getBytes())
						.parseClaimsJws(token.replace(TOKEN.PREFIX, ""))
						.getBody();

				String login = clains.get("login").toString();

				if (login != null) {
					return new UsernamePasswordAuthenticationToken(login, null, Collections.emptyList());
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		return null;
	}

}
