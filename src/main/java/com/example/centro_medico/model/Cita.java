package com.example.centro_medico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente; // Relación con la tabla Pacientes

    private String fechaCita;
    private String motivo;

}
