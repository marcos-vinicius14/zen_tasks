package com.marcos.dev.zentasks.zen_task_api.tasks.exceptions;

/**
 * Exceção lançada quando uma tentativa de acessar um recurso que não existe
 * é feita (ex: buscar um usuário por um ID que não está no banco de dados).
 * <p>
 * Esta é uma "unchecked exception", pois herda de RuntimeException.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Construtor que aceita uma mensagem de erro personalizada.
     * @param message A mensagem detalhando o erro.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Construtor sobrecarregado para criar uma mensagem padronizada.
     * @param resourceName O nome do recurso (ex: "Usuário", "Tarefa").
     * @param resourceId O ID do recurso que não foi encontrado.
     */
    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(String.format("Recurso '%s' com ID '%d' não encontrado.", resourceName, resourceId));

    }
}
