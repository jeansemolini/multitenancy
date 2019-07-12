package com.concept.sistema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concept.sistema.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{

}
