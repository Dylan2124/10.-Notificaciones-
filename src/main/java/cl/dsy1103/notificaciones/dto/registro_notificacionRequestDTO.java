package cl.dsy1103.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class registro_notificacionRequestDTO {

    @NotNull(message = "El id de usuario es obligatorio")
    @Positive(message = "El id debe ser mayor a 0")
    private Long idUsuario;

    @NotNull(message = "El id de pedido es obligatorio")
    @Positive(message = "El id debe ser mayor a 0")
    private Long idPedido;

    @NotBlank(message = "El tipo de notificación (EMAIL, SMS, etc.) no puede estar vacío")
    private String tipo;

    @NotBlank(message = "El mensaje de la notificación no puede estar vacío")
    private String mensaje;
}