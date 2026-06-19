package cl.dsy1103.notificaciones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class registro_notificacionResponseDTO extends RepresentationModel<registro_notificacionResponseDTO> {

    private Long idNotificacion;
    private Long idUsuario;
    private Long idPedido;
    private String tipo;
    private String mensaje;
    private LocalDateTime fechaEnvio;
}