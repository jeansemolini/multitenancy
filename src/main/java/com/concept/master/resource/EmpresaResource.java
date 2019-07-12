package com.concept.master.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.concept.master.model.Empresa;
import com.concept.master.model.EmpresaNova;
import com.concept.master.repository.EmpresaRepository;
import com.concept.util.JDBCUtil;

@RestController
@RequestMapping("/master")
public class EmpresaResource {
	@Autowired
	EmpresaRepository empresaRepository;

	@GetMapping("/empresas")
	private @ResponseBody ResponseEntity<?> buscarEmpresas(String nome) {
		return ResponseEntity.ok(empresaRepository.findAll());
	}

	@PostMapping("/cadastrar")
	private @ResponseBody ResponseEntity<?> cadastrarEmpresa(@RequestBody EmpresaNova empresaNova) {

		Optional<Empresa> empresa = empresaRepository.findByCnpj(empresaNova.getCnpj());

		if (empresa.isPresent()) {
			return ResponseEntity.ok("Empresa já cadastrada");
		}

		try {
			JDBCUtil.criarBancoDados(empresaNova);

			empresa = empresaRepository.findByCnpj(empresaNova.getCnpj());

			Flyway flyway = new Flyway();
			flyway.setLocations("db/sistema");
			flyway.setDataSource(empresa.get().getUrl(),empresa.get().getUsername(),empresa.get().getPassword());
			flyway.setBaselineOnMigrate(true);
			flyway.migrate();

			return ResponseEntity.status(HttpStatus.CREATED).body("Empresa cadastrada com sucesso");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
	}
	
}
