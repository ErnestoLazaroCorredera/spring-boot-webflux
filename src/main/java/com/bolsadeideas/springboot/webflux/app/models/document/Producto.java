package com.bolsadeideas.springboot.webflux.app.models.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "productos")
public class Producto {

  @Id
  private String id;

  @NonNull
  @NotEmpty
  @Field("nombre")
  private String nombre;

  @NonNull
  @NotNull
  @Field("precio")
  private BigDecimal precio;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Field("created_at")
  private Instant createAt;

}
