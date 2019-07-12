package com.concept.sistema.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.concept.sistema.repository.ProdutoRepository;

@RestController
//@RequestMapping("/{empresa}/produtos")
@RequestMapping("/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoRepository produtos;

	@GetMapping
	private @ResponseBody ResponseEntity<?> fitrar() {
		return ResponseEntity.ok(produtos.findAll());
	}

}
