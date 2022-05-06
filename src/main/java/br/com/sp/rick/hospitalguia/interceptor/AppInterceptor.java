package br.com.sp.rick.hospitalguia.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import br.com.sp.rick.hospitalguia.annotation.Privado;
import br.com.sp.rick.hospitalguia.annotation.Publico;
import br.com.sp.rick.hospitalguia.rest.UsuarioRestController;

@Component
public class AppInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// variavel para obter a URI da request
		String uri = request.getRequestURI();
		// variavel pra sessao
		HttpSession session = request.getSession();

		if (uri.startsWith("/error")) {
			return true;
		}

		// verificar se handler é um HandlerMethod, oq indica q ele está chamando um
		// metodo em algum controller
		if (handler instanceof HandlerMethod) {
			// casting de Object para HandlerMethod
			HandlerMethod metodo = (HandlerMethod) handler;
			if (uri.startsWith("/api")) {
				// variavel para o token
				String token = null;
				// verifica se o metodo é privado
				if (metodo.getMethodAnnotation(Privado.class) != null) {
					try {
						// se o metodo for privado recupera o token
						token = request.getHeader("Authorization");
						// cria o algoritmo para assinar
						Algorithm algoritmo = Algorithm.HMAC256(UsuarioRestController.SECRET);
						// objeto para verificar o token
						JWTVerifier verifier = JWT.require(algoritmo).withIssuer(UsuarioRestController.EMISSOR).build();
						// decodifica o Token
						DecodedJWT jwt = verifier.verify(token);
						//recupera os dados do payload
						Map<String, Claim> claims = jwt.getClaims();
						System.out.println(claims.get("name"));
						return true;
					} catch (Exception e) {
						if(token == null) {
							response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
						}else {
							response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
						}
						return false;
					}
				}
				return true;
			} else {
				// verifica se o metodo é publico
				if (metodo.getMethodAnnotation(Publico.class) != null) {
					return true;
				}
				// verifica se existe um usuario logado
				if (session.getAttribute("usuarioLogado") != null) {
					return true;
				}
				// redireciona para a pagina inical
				response.sendRedirect("/");
				return false;
			}
		}
		return true;
	}
}
