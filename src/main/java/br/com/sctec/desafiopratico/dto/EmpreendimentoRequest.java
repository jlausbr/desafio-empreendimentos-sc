package br.com.sctec.desafiopratico.dto;

import br.com.sctec.desafiopratico.model.SegmentoAtuacao;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para criação e atualização de empreendimentos.
 */
public record EmpreendimentoRequest(

        @NotBlank(message = "O nome do empreendimento é obrigatório")
        @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
        String nome,

        @NotBlank(message = "O nome do empreendedor é obrigatório")
        @Size(max = 200, message = "O nome do empreendedor deve ter no máximo 200 caracteres")
        String empreendedor,

        @NotBlank(message = "O município é obrigatório")
        @Size(max = 100, message = "O município deve ter no máximo 100 caracteres")
        String municipio,

        @NotNull(message = "O segmento de atuação é obrigatório")
        SegmentoAtuacao segmento,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        @Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres")
        String email,

        Boolean ativo
) {
}
