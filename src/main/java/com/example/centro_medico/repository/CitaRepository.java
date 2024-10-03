package com.example.centro_medico.repository;

import com.example.centro_medico.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CitaRepository extends JpaRepository<Cita, Long> {

}
