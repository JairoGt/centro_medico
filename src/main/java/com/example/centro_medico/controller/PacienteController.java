package com.example.centro_medico.controller;

import com.example.centro_medico.ResourceNotFoundException;
import com.example.centro_medico.model.Paciente;
import com.example.centro_medico.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    // Obtener todos los pacientes
    @GetMapping
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }

    // Crear un nuevo paciente
    @PostMapping
    public Paciente createPaciente(@RequestBody Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    // Obtener un paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id :: " + id));
        return ResponseEntity.ok(paciente);
    }

    // Actualizar un paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @RequestBody Paciente pacienteDetails) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id :: " + id));

        paciente.setNombre(pacienteDetails.getNombre());
        paciente.setApellido(pacienteDetails.getApellido());
        paciente.setFecha_Nacimiento(pacienteDetails.getFecha_Nacimiento());
        paciente.setGenero(pacienteDetails.getGenero());
        paciente.setDireccion(pacienteDetails.getDireccion());
        paciente.setTelefono(pacienteDetails.getTelefono());
        paciente.setCorreo(pacienteDetails.getCorreo());
        paciente.setHistorialMedico(pacienteDetails.getHistorialMedico());

        Paciente updatedPaciente = pacienteRepository.save(paciente);
        return ResponseEntity.ok(updatedPaciente);
    }

    // Eliminar un paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id :: " + id));

        pacienteRepository.delete(paciente);
        return ResponseEntity.noContent().build();
    }

}
