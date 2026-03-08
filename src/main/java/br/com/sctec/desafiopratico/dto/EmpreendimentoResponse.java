package br.com.sctec.desafiopratico.dto;

import java.time.LocalDateTime;

import br.com.sctec.desafiopratico.model.Empreendimento;
import br.com.sctec.desafiopratico.model.SegmentoAtuacao;

/**
 * DTO de saída com os dados do empreendimento para a API.
 */
public record EmpreendimentoResponse(
        Long id,
        String nome,
        String empreendedor,
        String municipio,
        SegmentoAtuacao segmento,
        String email,
        Boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    /**
     * Converte a entidade JPA para o DTO de resposta.
     */
    public static EmpreendimentoResponse fromEntity(Empreendimento entity) {
        return new EmpreendimentoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getEmpreendedor(),
                entity.getMunicipio(),
                entity.getSegmento(),
                entity.getEmail(),
                entity.getAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}
