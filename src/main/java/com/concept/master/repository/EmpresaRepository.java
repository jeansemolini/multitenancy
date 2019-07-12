package com.concept.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.concept.master.model.Empresa;


public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	public Optional<Empresa> findByCnpj(String cnpj);

	@Query("SELECT e FROM Empresa e WHERE e.cnpj = ?1")
	public Empresa findEmpresaByCnpjAndAtivo(String cnpj);
}
