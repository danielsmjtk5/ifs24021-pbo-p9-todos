
package org.delcom.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.delcom.app.entities.Todo;
import org.delcom.app.repositories.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TodoServiceTest {

    @Test
    @DisplayName("Pengujian lengkap untuk TodoService")
    void testTodoService() throws Exception {

        // ------------------------------------
        // Setup data
        // ------------------------------------
        UUID todoId = UUID.randomUUID();
        UUID nonexistentTodoId = UUID.randomUUID();

        Todo todo = new Todo("Belajar Spring Boot", "Belajar mock repository di unit test", false);
        todo.setId(todoId);

        // ------------------------------------
        // Mock repository
        // ------------------------------------
        TodoRepository todoRepository = Mockito.mock(TodoRepository.class);

        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        when(todoRepository.findByKeyword("Belajar")).thenReturn(List.of(todo));
        when(todoRepository.findAll()).thenReturn(List.of(todo));
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(todoRepository.findById(nonexistentTodoId)).thenReturn(Optional.empty());
        when(todoRepository.existsById(todoId)).thenReturn(true);
        when(todoRepository.existsById(nonexistentTodoId)).thenReturn(false);
        doNothing().when(todoRepository).deleteById(any(UUID.class));

        // ------------------------------------
        // Service instance
        // ------------------------------------
        TodoService todoService = new TodoService(todoRepository);
        assertNotNull(todoService);

        // ------------------------------------
        // Test createTodo
        // ------------------------------------
        Todo createdTodo = todoService.createTodo(todo.getTitle(), todo.getDescription());
        assertNotNull(createdTodo);
        assertEquals(todoId, createdTodo.getId());
        assertEquals(todo.getTitle(), createdTodo.getTitle());
        assertEquals(todo.getDescription(), createdTodo.getDescription());

        // ------------------------------------
        // Test getAllTodos tanpa search
        // ------------------------------------
        List<Todo> allTodos = todoService.getAllTodos(null);
        assertEquals(1, allTodos.size());

        // ------------------------------------
        // Test getAllTodos dengan search
        // ------------------------------------
        List<Todo> searchedTodos = todoService.getAllTodos("Belajar");
        assertEquals(1, searchedTodos.size());

        // Search hanya whitespace → dianggap fetch all
        List<Todo> whitespaceSearch = todoService.getAllTodos("     ");
        assertEquals(1, whitespaceSearch.size());

        // ------------------------------------
        // Test getTodoById (data ada)
        // ------------------------------------
        Todo fetchedTodo = todoService.getTodoById(todoId);
        assertNotNull(fetchedTodo);
        assertEquals(todoId, fetchedTodo.getId());
        assertEquals(todo.getTitle(), fetchedTodo.getTitle());
        assertEquals(todo.getDescription(), fetchedTodo.getDescription());

        // ------------------------------------
        // Test getTodoById (data tidak ada)
        // ------------------------------------
        Todo nonExistentFetchedTodo = todoService.getTodoById(nonexistentTodoId);
        assertNull(nonExistentFetchedTodo);

        // ------------------------------------
        // Test updateTodo (data ada)
        // ------------------------------------
        String updatedTitle = "Belajar Spring Boot Lanjutan";
        String updatedDescription = "Belajar mock repository di unit test dengan Mockito";
        boolean updatedIsFinished = true;

        Todo updatedTodo = todoService.updateTodo(
                todoId,
                updatedTitle,
                updatedDescription,
                updatedIsFinished
        );

        assertNotNull(updatedTodo);
        assertEquals(updatedTitle, updatedTodo.getTitle());
        assertEquals(updatedDescription, updatedTodo.getDescription());
        assertEquals(updatedIsFinished, updatedTodo.isFinished());

        // ------------------------------------
        // Test updateTodo (data tidak ada)
        // ------------------------------------
        Todo nonExistentUpdated = todoService.updateTodo(
                nonexistentTodoId,
                updatedTitle,
                updatedDescription,
                updatedIsFinished
        );
        assertNull(nonExistentUpdated);

        // ------------------------------------
        // Test deleteTodo (data ada)
        // ------------------------------------
        boolean deleted = todoService.deleteTodo(todoId);
        assertTrue(deleted);

        // ------------------------------------
        // Test deleteTodo (data tidak ada)
        // ------------------------------------
        boolean deleteNonExistent = todoService.deleteTodo(nonexistentTodoId);
        assertFalse(deleteNonExistent);
    }
}
