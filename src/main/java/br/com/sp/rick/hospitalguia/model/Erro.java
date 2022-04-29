package br.com.sp.rick.hospitalguia.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class Erro {
	private HttpStatus status;
	private String mensagem;
	private String exception;
	
	public Erro(HttpStatus status, String msg, String exc) {
		this.status = status;
		this.mensagem = msg;
		this.exception = exc;
	}
}
