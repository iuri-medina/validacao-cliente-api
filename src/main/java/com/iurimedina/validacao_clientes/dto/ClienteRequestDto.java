package com.iurimedina.validacao_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteRequestDto {

	@NotBlank(message = "O campo NOME não pode estar vazio.")
	private String nome;
	
	@NotBlank(message = "O campo CNPJ não pode estar vazio.")
	private String cnpj;
	
	@NotNull(message = "O campo ATIVO não pode estar vazio.")
	private Boolean ativo;
}
