package com.concept.master.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.concept.master.repository.EmpresaRepository;

@RestController
public class UsuarioResource {

	@Autowired
	public EmpresaRepository empresaRepository;

	@PostMapping("/login")
	private @ResponseBody ResponseEntity<?> login(String nome) {
		return ResponseEntity.ok(empresaRepository.findAll());
	}

}
