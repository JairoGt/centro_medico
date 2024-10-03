package com.example.centro_medico.repository;

import com.example.centro_medico.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

}
