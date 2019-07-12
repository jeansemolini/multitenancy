package com.concept.tenant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.concept.constants.AppConstants.TOKEN;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class TenancyInterceptor extends HandlerInterceptorAdapter {

	private static final String EMPRESA_TENANT = "EMPRESA_TENANT";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String token = request.getHeader(TOKEN.HEADER_STRING);

		if (token != null) {
			try {
				Claims clains = Jwts.parser()
						.setSigningKey(TOKEN.SECRET.getBytes())
						.parseClaimsJws(token.replace(TOKEN.PREFIX, ""))
						.getBody();

				//String login = clains.get("login").toString();
				String empresa = clains.get("empresa").toString();
				//String nome = clains.get("nome").toString();

				if (empresa != null) {
					request.setAttribute(EMPRESA_TENANT, empresa);
					return true;
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		return false;
	}

	public static String getEmpresa() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			return (String) attributes.getAttribute(EMPRESA_TENANT, RequestAttributes.SCOPE_REQUEST);
		}
		return null;
	}

}
