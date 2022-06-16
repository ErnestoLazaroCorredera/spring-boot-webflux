package com.bolsadeideas.springboot.webflux.app.models.document;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "productos")
public class Producto {

  @Id
  private String id;

  @NotEmpty
  @Field("nombre")
  private String nombre;

  @NotNull
  @Field("precio")
  private BigDecimal precio;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Field("created_at")
  private Date createAt;

}
