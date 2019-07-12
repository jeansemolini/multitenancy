package com.concept.master.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concept.master.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	public Optional<Usuario> findByLogin(String login);

}
