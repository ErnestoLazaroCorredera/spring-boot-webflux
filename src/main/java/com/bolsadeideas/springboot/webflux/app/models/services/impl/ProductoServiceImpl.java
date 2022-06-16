package com.bolsadeideas.springboot.webflux.app.models.services.impl;

import com.bolsadeideas.springboot.webflux.app.models.document.Producto;
import com.bolsadeideas.springboot.webflux.app.models.repository.ProductoRepository;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

  @Autowired
  private ProductoRepository repo;

  @Override
  public Flux<Producto> findAll() {
    return repo.findAll();
  }

  @Override
  public Flux<Producto> findAllConNombreUpperCase() {
    return repo.findAll().map(p -> {
      p.setNombre(p.getNombre().toUpperCase());
      return p;
    });
  }

  @Override
  public Flux<Producto> findAllConNombreUpperCaseRepeat() {
    return findAllConNombreUpperCase().repeat(5000);
  }

  @Override
  public Mono<Producto> findById(String id) {
    return repo.findById(id);
  }

  @Override
  public Mono<Producto> save(Producto producto) {
    return repo.save(producto);
  }

  @Override
  public Mono<Void> delete(Producto producto) {
    return repo.delete(producto);
  }
}
