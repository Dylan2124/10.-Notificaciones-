package cl.dsy1103.notificaciones.controller;

import cl.dsy1103.notificaciones.dto.registro_notificacionRequestDTO;
import cl.dsy1103.notificaciones.dto.registro_notificacionResponseDTO;
import cl.dsy1103.notificaciones.service.registro_notificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class registro_notificacionController {

    private final registro_notificacionService service;

    // ── GET  ────────────────────────────────────
    @GetMapping
    public ResponseEntity<?> obtenerTodas() {
        List<registro_notificacionResponseDTO> lista = service.obtenerTodas();
        if (lista.isEmpty()){
            return ResponseEntity.ok(Map.of("mensaje", "No se encontraron notificaciones"));
        }

        lista.forEach(dto -> dto.add(linkTo(methodOn(registro_notificacionController.class).obtenerPorId(dto.getIdNotificacion())).withSelfRel()));
        CollectionModel<registro_notificacionResponseDTO> collectionModel = CollectionModel.of(lista,
                linkTo(methodOn(registro_notificacionController.class).obtenerTodas()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // ── GET: OBTENER POR ID  ───────
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<registro_notificacionResponseDTO> opt = service.obtenerPorId(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró la notificación con el ID: " + id));
        }

        registro_notificacionResponseDTO response = opt.get();
        response.add(linkTo(methodOn(registro_notificacionController.class).obtenerPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(registro_notificacionController.class).obtenerTodas()).withRel("todas-las-notificaciones"));

        return ResponseEntity.ok(response);
    }

    // ── GET: OBTENER NOTIFICACIONES POR USUARIO ───────
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        List<registro_notificacionResponseDTO> lista = service.obtenerPorUsuario(idUsuario);
        if (lista.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontraron notificaciones para el usuario: " + idUsuario));
        }

        lista.forEach(dto -> dto.add(linkTo(methodOn(registro_notificacionController.class).obtenerPorId(dto.getIdNotificacion())).withSelfRel()));
        CollectionModel<registro_notificacionResponseDTO> collectionModel = CollectionModel.of(lista,
                linkTo(methodOn(registro_notificacionController.class).obtenerPorUsuario(idUsuario)).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // ── POST: CREAR NOTIFICACIÓN ──────────────────────────────
    @PostMapping
    public ResponseEntity<registro_notificacionResponseDTO> guardar(
            @Valid @RequestBody registro_notificacionRequestDTO request) {
        registro_notificacionResponseDTO response = service.guardar(request);
        response.add(linkTo(methodOn(registro_notificacionController.class).obtenerPorId(response.getIdNotificacion())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── PUT: ACTUALIZAR ───────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody registro_notificacionRequestDTO request) {

        Optional<registro_notificacionResponseDTO> actualizado = service.actualizar(id, request);

        if (actualizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Error al actualizar: La notificación con ID " + id + " no existe."));
        }

        registro_notificacionResponseDTO response = actualizado.get();
        response.add(linkTo(methodOn(registro_notificacionController.class).obtenerPorId(id)).withSelfRel());
        response.add(linkTo(methodOn(registro_notificacionController.class).obtenerTodas()).withRel("todas-las-notificaciones"));

        return ResponseEntity.ok(response);
    }

    // ── DELETE: ELIMINAR ──────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (service.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se puede eliminar. No existe notificación con ID: " + id));
        }

        service.eliminar(id);
        return ResponseEntity.ok(Map.of("mensaje", "Notificación con ID " + id + " eliminada con éxito."));
    }
}