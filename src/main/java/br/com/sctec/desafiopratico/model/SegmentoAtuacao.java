package br.com.sctec.desafiopratico.model;

/**
 * Segmentos de atuação permitidos para os empreendimentos catarinenses.
 */
public enum SegmentoAtuacao {

    TECNOLOGIA("Tecnologia"),
    COMERCIO("Comércio"),
    INDUSTRIA("Indústria"),
    SERVICOS("Serviços"),
    AGRONEGOCIO("Agronegócio");

    private final String descricao;

    SegmentoAtuacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
