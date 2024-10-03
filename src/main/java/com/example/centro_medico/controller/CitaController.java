package com.example.centro_medico.controller;

import com.example.centro_medico.ResourceNotFoundException;
import com.example.centro_medico.model.Cita;
import com.example.centro_medico.model.Paciente;
import com.example.centro_medico.repository.CitaRepository;
import com.example.centro_medico.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Obtener todas las citas
    @GetMapping
    public List<Cita> getAllCitas() {
        return citaRepository.findAll();
    }

    // Crear una nueva cita
    @PostMapping
    public Cita createCita(@RequestBody Cita cita) {
        // Validar que el paciente existe
        if (cita.getPaciente() == null || cita.getPaciente().getId() == null) {
            throw new IllegalArgumentException("La cita debe tener un paciente válido asociado");
        }

        // Buscar el paciente en la base de datos
        Paciente paciente = pacienteRepository.findById(cita.getPaciente().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente no encontrado con id :: " + cita.getPaciente().getId()));

        // Asignar el paciente a la cita
        cita.setPaciente(paciente);

        // Guardar la cita
        return citaRepository.save(cita);
    }

    // Obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getCitaById(@PathVariable Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id :: " + id));
        return ResponseEntity.ok(cita);
    }

    // Actualizar una cita
    @PutMapping("/{id}")
    public ResponseEntity<Cita> updateCita(@PathVariable Long id, @RequestBody Cita citaDetails) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id :: " + id));

        // Actualizamos los campos de la cita
        cita.setFechaCita(citaDetails.getFechaCita());
        cita.setMotivo(citaDetails.getMotivo());

        // Validar si se está actualizando el paciente
        if (citaDetails.getPaciente() != null && citaDetails.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(citaDetails.getPaciente().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Paciente no encontrado con id :: " + citaDetails.getPaciente().getId()));
            cita.setPaciente(paciente);
        }

        Cita updatedCita = citaRepository.save(cita);
        return ResponseEntity.ok(updatedCita);
    }

    // Eliminar una cita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(@PathVariable Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id :: " + id));

        citaRepository.delete(cita);
        return ResponseEntity.noContent().build();
    }
}