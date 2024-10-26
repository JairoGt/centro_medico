package com.example.centro_medico.controller;

import com.example.centro_medico.ResourceNotFoundException;
import com.example.centro_medico.dto.LoginRequest;
import com.example.centro_medico.model.Usuario;
import com.example.centro_medico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    


     private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Crear un nuevo usuario
    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id :: " + id));
        return ResponseEntity.ok(usuario);
    }

    // Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id :: " + id));

        usuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
        usuario.setContrasena(usuarioDetails.getContrasena());
        usuario.setRol(usuarioDetails.getRol());

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(updatedUsuario);
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id :: " + id));

        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }

    // Login de usuario
@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUsuario(loginRequest.getNombreUsuario());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getContrasena().equals(loginRequest.getContrasena())) {
                // Obtener y configurar la sesión
                HttpSession session = getSession();
                session.setAttribute("userId", usuario.getId());
                session.setAttribute("username", usuario.getNombreUsuario());
                session.setAttribute("rol", usuario.getRol());
                
                return ResponseEntity.ok("Login exitoso");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @GetMapping("/check-auth")
    public ResponseEntity<String> checkAuth() {
        try {
            HttpSession session = getSession();
            Object userId = session.getAttribute("userId");
            if (userId != null) {
                return ResponseEntity.ok("Autenticado");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            HttpSession session = getSession();
            session.invalidate();
            return ResponseEntity.ok("Logout exitoso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar sesión");
        }
    }


}