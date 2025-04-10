package com.iurimedina.validacao_clientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(unique = true, nullable = false, updatable = false) // chave unica e imutavel
	private String chave;
	
	@Column(unique = true, nullable = false)
	private String cnpj;

	@Column(nullable = false)
	private boolean ativo;

	public Cliente(String nome, String cnpj, boolean ativo) {
		this.nome = nome;
		this.cnpj = cnpj;
		this.ativo = ativo;
	}
	
	public Cliente() {}
	
	
}
