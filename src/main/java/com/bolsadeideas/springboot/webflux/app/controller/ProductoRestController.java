package com.bolsadeideas.springboot.webflux.app.controller;

import com.bolsadeideas.springboot.webflux.app.models.repository.ProductoRepository;
import com.bolsadeideas.springboot.webflux.app.models.document.Producto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

  @Autowired
  private ProductoRepository repo;

  @GetMapping
  public Flux<Producto> index() {
    return repo.findAll().map(p -> {
      p.setNombre(p.getNombre().toUpperCase());
      return p;
    }).doOnNext(p -> log.info(p.getNombre()));
  }

  @GetMapping("/{id}")
  public Mono<Producto> show(@PathVariable String id) {
    return repo.findAll().filter(p -> p.getId().equals(id)).next();
  }


}
