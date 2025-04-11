package com.iurimedina.validacao_clientes.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidacaoRequestDto {

	@NotBlank(message = "O campo Chave não pode estar vazio.")
	private String chave;
	
	@NotBlank(message = "O campo CNPJ não pode estar vazio")
	private String cnpj;
	

}
