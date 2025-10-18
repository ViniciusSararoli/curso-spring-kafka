package io.github.cursodsousa.icompras.produtos.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "produto")
@Data
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproduto;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "valor", nullable = false, precision = 200, scale = 2)
    private BigDecimal valor;
}
