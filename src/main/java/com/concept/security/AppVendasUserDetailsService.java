package com.concept.security;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.concept.master.model.Empresa;
import com.concept.master.model.Usuario;
import com.concept.master.repository.EmpresaRepository;
import com.concept.master.repository.UsuarioRepository;

@Service
public class AppVendasUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(username);

		usuarioOptional.get().setSenha(passwordEncoder.encode(usuarioOptional.get().getSenha()));

		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha inválidos"));

		Optional<Empresa> empresa = empresaRepository.findByCnpj(usuario.getEmpresa());

		if (!empresa.get().isAtivo()) {
			usuario.setAtivo(false);
		}

		//usuario.setSenha(passwordEncoder.encode(usuario.getSenha().trim()));

		return new UsuarioSistema(usuario, Collections.emptyList());
	}



}
