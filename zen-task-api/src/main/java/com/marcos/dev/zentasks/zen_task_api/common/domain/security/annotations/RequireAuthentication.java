package com.marcos.dev.zentasks.zen_task_api.common.domain.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar métodos que requerem autenticação.
 * Faz parte do Shared Kernel para ser utilizada por todos os módulos.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuthentication {
  String message() default "Usuário não autenticado";

  String[] roles() default {};
}
