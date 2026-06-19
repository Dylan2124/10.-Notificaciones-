package cl.dsy1103.notificaciones.service;

import cl.dsy1103.notificaciones.client.UsuarioClient;
import cl.dsy1103.notificaciones.dto.UsuarioExternoDTO;
import cl.dsy1103.notificaciones.dto.registro_notificacionRequestDTO;
import cl.dsy1103.notificaciones.dto.registro_notificacionResponseDTO;
import cl.dsy1103.notificaciones.model.registro_notificacion;
import cl.dsy1103.notificaciones.repository.registro_notificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class registro_notificacionServiceTest {

    @Mock
    private registro_notificacionRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private registro_notificacionService service;

    private registro_notificacion notificacion;
    private registro_notificacionRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        notificacion = new registro_notificacion();
        notificacion.setIdNotificacion(1L);
        notificacion.setIdUsuario(10L);
        notificacion.setIdPedido(100L);
        notificacion.setTipo("INFO");
        notificacion.setMensaje("Tu pedido está en camino");
        notificacion.setFechaEnvio(LocalDateTime.now());

        requestDTO = new registro_notificacionRequestDTO();
        requestDTO.setIdUsuario(10L);
        requestDTO.setIdPedido(100L);
        requestDTO.setTipo("INFO");
        requestDTO.setMensaje("Tu pedido está en camino");
    }

    @Test
    void obtenerTodas() {
        when(repository.findAll()).thenReturn(Arrays.asList(notificacion));
        List<registro_notificacionResponseDTO> result = service.obtenerTodas();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void obtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        Optional<registro_notificacionResponseDTO> result = service.obtenerPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getIdNotificacion());
    }

    @Test
    void obtenerPorUsuario() {
        when(repository.findByIdUsuario(10L)).thenReturn(Arrays.asList(notificacion));
        List<registro_notificacionResponseDTO> result = service.obtenerPorUsuario(10L);
        assertFalse(result.isEmpty());
        assertEquals(10L, result.get(0).getIdUsuario());
    }

    @Test
    void guardarExito() {
        UsuarioExternoDTO usuario = new UsuarioExternoDTO();
        usuario.setEmail("test@test.com");
        usuario.setNombreCompleto("Juan Perez");

        when(usuarioClient.obtenerUsuarioPorId(10L)).thenReturn(usuario);
        when(repository.save(any(registro_notificacion.class))).thenReturn(notificacion);

        registro_notificacionResponseDTO result = service.guardar(requestDTO);

        assertNotNull(result);
        assertEquals("INFO", result.getTipo());
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(10L);
    }

    @Test
    void guardarToleranciaFallos() {
        // Simulamos que el microservicio de usuarios está caído
        when(usuarioClient.obtenerUsuarioPorId(10L)).thenThrow(new RuntimeException("Error Feign"));
        when(repository.save(any(registro_notificacion.class))).thenReturn(notificacion);

        registro_notificacionResponseDTO result = service.guardar(requestDTO);

        // Se debe guardar igual a pesar del fallo
        assertNotNull(result);
        assertEquals("INFO", result.getTipo());
        verify(usuarioClient, times(1)).obtenerUsuarioPorId(10L);
    }

    @Test
    void actualizar() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(registro_notificacion.class))).thenReturn(notificacion);

        Optional<registro_notificacionResponseDTO> result = service.actualizar(1L, requestDTO);

        assertTrue(result.isPresent());
        assertEquals("Tu pedido está en camino", result.get().getMensaje());
    }

    @Test
    void eliminar() {
        doNothing().when(repository).deleteById(1L);
        service.eliminar(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}