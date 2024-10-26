package com.example.centro_medico.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String genero;
    @Column(name = "fecha_nacimiento")
    private String fecha_nacimiento;
    private String direccion;
    private String telefono;
    private String correo;
    private String historialMedico;

}
