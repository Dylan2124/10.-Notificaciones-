package cl.dsy1103.notificaciones.repository;

import cl.dsy1103.notificaciones.model.registro_notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface registro_notificacionRepository extends JpaRepository<registro_notificacion, Long> {

    // Buscar todas las notificaciones de un usuario específico
    List<registro_notificacion> findByIdUsuario(Long idUsuario);

    // Buscar las notificaciones de un pedido
    List<registro_notificacion> findByIdPedido(Long idPedido);
}