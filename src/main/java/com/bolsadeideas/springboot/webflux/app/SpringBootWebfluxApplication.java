package com.bolsadeideas.springboot.webflux.app;

import java.math.BigDecimal;
import java.time.Instant;

import com.bolsadeideas.springboot.webflux.app.models.repository.ProductoRepository;
import com.bolsadeideas.springboot.webflux.app.models.document.Producto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

@Log4j2
@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoRepository repository;
	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection(Producto.class).subscribe();
		Flux.just(new Producto("TV Panasonic Pantalla LCD", BigDecimal.valueOf(456.89)),
						new Producto("Sony Camara HD Digital", BigDecimal.valueOf(177.89)),
						new Producto("Apple iPod", BigDecimal.valueOf(46.89)),
						new Producto("Sony Notebook", BigDecimal.valueOf(846.89)),
						new Producto("Hewlett Packard Multifuncional", BigDecimal.valueOf(200.89)),
						new Producto("Bianchi Bicicleta", BigDecimal.valueOf(70.89)),
						new Producto("HP Notebook Omen 17", BigDecimal.valueOf(2500.89)),
						new Producto("Mica Cómoda 5 Cajones", BigDecimal.valueOf(150.89)),
						new Producto("TV Sony Bravia OLED 4K Ultra HD", BigDecimal.valueOf(2255.89)))
						.flatMap(producto -> {
							producto.setCreateAt(Instant.now());
							return repository.save(producto);
						})
						.subscribe(producto -> log.info("Insert producto con Id: {}, y nombre {}",
										producto.getId(), producto.getNombre()));
	}
}
