package com.bolsadeideas.springboot.webflux.app.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import com.bolsadeideas.springboot.webflux.app.models.document.Producto;
import com.bolsadeideas.springboot.webflux.app.models.services.ProductoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@SessionAttributes("producto")
@Controller
public class ProductoController {

  @Autowired
  private ProductoService productoService;

  @GetMapping({"/listar", "/"})
  public Mono<String> listar(final Model model) {
    final Flux<Producto> productos = productoService.findAllConNombreUpperCase();

    productos.subscribe(p -> log.info(p.getNombre()));

    model.addAttribute("productos", productos);
    model.addAttribute("titulo", "Listado de productos");
    return Mono.just("listar");
  }

  @GetMapping("/form")
  public Mono<String> crear(final Model model) {
    model.addAttribute("producto", new Producto());
    model.addAttribute("titulo", "Formulario de producto");
    model.addAttribute("button", "Crear");
    return Mono.just("form");
  }

  @PostMapping("/form")
  public Mono<String> guardar(@Valid @ModelAttribute("producto") final Producto producto,
                              final BindingResult result,
                              final Model model,
                              final SessionStatus sessionStatus) {
    if(result.hasErrors()) {
      model.addAttribute("titulo", "Errores en formulario Producto");
      model.addAttribute("button", "Guardar");
      return Mono.just("form");
    }
    sessionStatus.setComplete();
    return productoService.save(producto).doOnNext(p -> {
      Optional.ofNullable(p.getCreateAt()).orElseGet(Instant::now);
      log.info("Producto guardado: {}, Id: {}", p.getNombre(), p.getId());
    }).thenReturn("redirect:/listar?success=producto+guardado+con+existo");
  }

  @GetMapping("eliminar/{id}")
  public Mono<String> eliminar(@PathVariable String id) {
    return productoService.findById(id)
            .defaultIfEmpty(new Producto())
            .flatMap(p -> {
              if (null == p.getId()) {
                return Mono.error(new InterruptedException("No existe el producto a eliminar!"));
              }
              return Mono.just(p);
            })
            .flatMap(p -> productoService.delete(p))
            .then(Mono.just("redirect:/listar?success=producto+eliminado+con+éxito"))
            .onErrorResume(ex -> Mono.just("redirect:/listar?error=no+existe+el+producto+a+eliminar"));
  }

  @GetMapping("/form/{id}")
  public Mono<String> editar(@PathVariable(name = "id") final String productoId, final Model model) {
    Mono<Producto> productoMono = productoService.findById(productoId).doOnNext(p ->
      log.info("Producto: {}", p.getNombre())
    ).defaultIfEmpty(new Producto());
    model.addAttribute("titulo", "Editar Producto");
    model.addAttribute("producto", productoMono);
    model.addAttribute("button", "Editar");
    return Mono.just("form");
  }

  @GetMapping("/form-v2/{id}")
  public Mono<String> editarV2(@PathVariable(name = "id") final String productoId, final Model model) {
    return productoService.findById(productoId)
            .doOnNext(p -> {
              model.addAttribute("producto", p);
              model.addAttribute("button", "Editar");
              log.info("Producto: {}", p.getNombre());
            })
            .defaultIfEmpty(new Producto())
            .flatMap(p -> {
              if (null == p.getId()) {
                return Mono.error(new InterruptedException());
              }
              return Mono.just(p);
            })
            .then(Mono.just("form"))
            .onErrorResume(ex -> Mono.just("redirect:/listar?error=no+existe+el+producto"));
  }

  @GetMapping("/listar-data-driver")
  public String listarDataDriver(final Model model) {
    final Flux<Producto> productos = productoService.findAllConNombreUpperCase().delayElements(Duration.ofNanos(1));

    productos.subscribe(p -> log.info(p.getNombre()));

    model.addAttribute("productos",
            new ReactiveDataDriverContextVariable(productos,2));
    model.addAttribute("titulo", "Listado de productos");
    return "listar";
  }

  @GetMapping("/listar-full")
  public String listarFull(final Model model) {
    final Flux<Producto> productos = productoService.findAllConNombreUpperCaseRepeat();

    model.addAttribute("productos", productos);
    model.addAttribute("titulo", "Listado de productos");
    return "listar";
  }

  @GetMapping("/listar-chunked")
  public String listarChunked(final Model model) {
    final Flux<Producto> productos = productoService.findAllConNombreUpperCaseRepeat();

    model.addAttribute("productos", productos);
    model.addAttribute("titulo", "Listado de productos");
    return "listar-chunked";
  }
}
