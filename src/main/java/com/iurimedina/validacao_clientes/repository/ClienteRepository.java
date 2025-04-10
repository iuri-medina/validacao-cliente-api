package com.iurimedina.validacao_clientes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iurimedina.validacao_clientes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	Optional<Cliente> findByChaveAndCnpjAndAtivoIsTrue(String chave, String cnpj);

	Optional<Cliente> findByChave(String chave);
	
	Optional<Cliente> findByCnpj(String cnpj);
	
	boolean existsByCnpj(String cnpj);
}
