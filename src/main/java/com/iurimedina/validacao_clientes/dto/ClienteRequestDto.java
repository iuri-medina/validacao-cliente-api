package com.iurimedina.validacao_clientes.dto;

import lombok.Data;

@Data
public class ClienteRequestDto {

	private String nome;
	private String cnpj;
	private boolean ativo;
}
