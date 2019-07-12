package com.concept.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AppVendasExceptionHandler extends ResponseEntityExceptionHandler  {

	@ExceptionHandler({ EmpresaJaExisteException.class })
	public ResponseEntity<Object> handleEmpresaJaExisteException(EmpresaJaExisteException ex, WebRequest request){
		List<Erro> erros = Arrays.asList(new Erro("Empresa já cadastrada"));

		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.OK, request);
	}

	public static class Erro {

		private String mensagem;

		public Erro(String mensagem) {
			this.mensagem = mensagem;
		}

		public String getMensagem() {
			return mensagem;
		}

		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}

	}

}
