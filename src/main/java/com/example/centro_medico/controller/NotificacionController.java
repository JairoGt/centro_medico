package com.example.centro_medico.controller;

import com.example.centro_medico.ResourceNotFoundException;
import com.example.centro_medico.model.Notificacion;
import com.example.centro_medico.model.Paciente;
import com.example.centro_medico.repository.NotificacionRepository;
import com.example.centro_medico.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Obtener todas las notificaciones
    @GetMapping
    public List<Notificacion> getAllNotificaciones() {
        return notificacionRepository.findAll();
    }

    // Crear una nueva notificación
    @PostMapping
    public Notificacion createNotificacion(@RequestBody Notificacion notificacion) {
        // Validar que el paciente existe
        if (notificacion.getPaciente() == null || notificacion.getPaciente().getId() == null) {
            throw new IllegalArgumentException("La notificación debe tener un paciente válido asociado");
        }

        // Buscar el paciente en la base de datos
        Paciente paciente = pacienteRepository.findById(notificacion.getPaciente().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente no encontrado con id :: " + notificacion.getPaciente().getId()));

        // Asignar el paciente a la notificación
        notificacion.setPaciente(paciente);

        // Guardar la notificación
        return notificacionRepository.save(notificacion);
    }

    // Obtener una notificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> getNotificacionById(@PathVariable Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id :: " + id));
        return ResponseEntity.ok(notificacion);
    }

    // Actualizar una notificación
    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> updateNotificacion(@PathVariable Long id,
            @RequestBody Notificacion notificacionDetails) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id :: " + id));

        notificacion.setMensaje(notificacionDetails.getMensaje());

        Notificacion updatedNotificacion = notificacionRepository.save(notificacion);
        return ResponseEntity.ok(updatedNotificacion);
    }

    // Eliminar una notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id :: " + id));

        notificacionRepository.delete(notificacion);
        return ResponseEntity.noContent().build();
    }
}