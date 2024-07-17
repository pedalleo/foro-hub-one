package com.ForoHub.ChallenAlura.controllers;

import com.ForoHub.ChallenAlura.dto.TopicoResponseDTO;
import com.ForoHub.ChallenAlura.models.Topico;
import com.ForoHub.ChallenAlura.repositories.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @GetMapping
    public List<Topico> listar() {
        return topicoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Topico> registrar(@Valid @RequestBody Topico topico, BindingResult result, UriComponentsBuilder uriBuilder) throws MethodArgumentNotValidException {
        if (result.hasErrors()) {
            throw new MethodArgumentNotValidException(null, result);
        }

        // Verificar si existe un tópico duplicado
        Optional<Topico> topicoExistente = topicoRepository.findByTituloAndMensaje(topico.getTitulo(), topico.getMensaje());
        if (topicoExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tópico duplicado");
        }

        Topico nuevoTopico = topicoRepository.save(topico);

        // Construir la URI del nuevo recurso
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(nuevoTopico.getId()).toUri();

        return ResponseEntity.created(uri).body(nuevoTopico);
    }

    @GetMapping("/{id}")
    public Topico detalle(@PathVariable Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));
    }

    @PutMapping("/{id}")
    public Topico actualizar(@PathVariable Long id, @Valid @RequestBody Topico datosActualizados) {
        Topico topicoExistente = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        // Actualizar los campos del tópico existente con los nuevos datos
        topicoExistente.setTitulo(datosActualizados.getTitulo());
        topicoExistente.setMensaje(datosActualizados.getMensaje());
        topicoExistente.setStatus(datosActualizados.getStatus());
        topicoExistente.setAutor(datosActualizados.getAutor());
        topicoExistente.setCurso(datosActualizados.getCurso());

        return topicoRepository.save(topicoExistente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TopicoResponseDTO> eliminar(@PathVariable Long id) {
        Topico topicoExistente = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico no encontrado"));

        topicoRepository.deleteById(id);

        TopicoResponseDTO response = new TopicoResponseDTO(
                topicoExistente.getTitulo(),
                topicoExistente.getMensaje(),
                topicoExistente.getStatus(),
                topicoExistente.getAutor(),
                topicoExistente.getCurso()
        );

        return ResponseEntity.ok(response);
    }

    // Listado paginado y ordenado por fecha de creación
    @GetMapping("/paginado")
    public Page<Topico> listarPaginado(
            @PageableDefault(sort = "fechaCreacion", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        return topicoRepository.findAll(pageable);
    }

    // Listado filtrado por curso y año específico
    @GetMapping("/filtrado")
    public List<Topico> listarFiltrado(@RequestParam String curso, @RequestParam int year) {
        return topicoRepository.findByCursoAndYear(curso, year);
    }
}
