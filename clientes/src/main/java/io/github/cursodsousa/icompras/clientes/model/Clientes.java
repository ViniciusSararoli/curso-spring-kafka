package io.github.cursodsousa.icompras.clientes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cliente")
@Data
public class Clientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcliente;

    @Column(name = "nome", nullable = true, length = 200)
    private String nome;

    @Column(name = "cpf_cnpj", nullable = false, length = 14)
    private String cpfCnpj;

    @Column(name = "telefone", nullable = true, length = 11)
    private String telefone;

    @Column(name = "email", nullable = true, length = 200)
    private String email;
}
