package com.example.centro_medico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notificaciones")

public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente; // Relación con la tabla Pacientes

    private String mensaje;
}
