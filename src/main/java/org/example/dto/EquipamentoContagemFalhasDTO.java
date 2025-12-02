package org.example.dto;

public class EquipamentoContagemFalhasDTO {
    private Long equipamentoId;
    private String nomeEquipamento;
    private int totalFalhas;

    public EquipamentoContagemFalhasDTO(String nomeEquipamento, int totalFalhas) {
        this.equipamentoId = equipamentoId;
        this.nomeEquipamento = nomeEquipamento;
        this.totalFalhas = totalFalhas;
    }

    public Long getEquipamentoId() { return equipamentoId; }
    public String getNomeEquipamento() { return nomeEquipamento; }
    public int getTotalFalhas() { return totalFalhas; }
}
