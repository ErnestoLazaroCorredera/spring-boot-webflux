package com.bolsadeideas.springboot.webflux.app.models.repository;

import com.bolsadeideas.springboot.webflux.app.models.document.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {
}
