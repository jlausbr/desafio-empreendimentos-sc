package br.com.sctec.desafiopratico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sctec.desafiopratico.model.Empreendimento;
import br.com.sctec.desafiopratico.model.SegmentoAtuacao;

/**
 * Repositório Spring Data JPA para a entidade Empreendimento.
 * Fornece operações CRUD padrão e consultas derivadas por convenção.
 */
@Repository
public interface EmpreendimentoRepository extends JpaRepository<Empreendimento, Long> {

    List<Empreendimento> findByMunicipioIgnoreCase(String municipio);

    List<Empreendimento> findBySegmento(SegmentoAtuacao segmento);

    List<Empreendimento> findByAtivo(Boolean ativo);

    List<Empreendimento> findByNomeContainingIgnoreCase(String nome);
}
