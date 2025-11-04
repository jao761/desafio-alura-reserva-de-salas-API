package jao761.reserva_de_salas_API.service;

import jao761.reserva_de_salas_API.dto.UsuarioAtualizarDTO;
import jao761.reserva_de_salas_API.dto.UsuarioCadastroDTO;
import jao761.reserva_de_salas_API.dto.UsuarioDetalheDTO;
import jao761.reserva_de_salas_API.dto.UsuarioListarDTO;
import jao761.reserva_de_salas_API.model.Usuario;
import jao761.reserva_de_salas_API.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve salvar usuário ao cadastrar")
    void cadastarUsuario_deveSalvarUsuario() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO("Joao", "Pontel", "joao@email.com", "senha123");

        // Act
        Usuario usuario = service.cadastarUsuario(dto);

        // Assert
        assertNotNull(usuario);
        assertEquals("Joao Pontel", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve listar usuários ativos paginados")
    void listarPaginado_deveRetornarDTO() {
        // Arrange
        Usuario u1 = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        u1.setId(1L);
        List<Usuario> usuarios = List.of(u1);
        Page<Usuario> page = new PageImpl<>(usuarios);
        when(repository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(page);

        // Act
        Page<UsuarioListarDTO> result = service.listarPaginado(Pageable.unpaged());

        // Assert
        assertEquals(1, result.getTotalElements());
        UsuarioListarDTO dto = result.getContent().get(0);
        assertEquals("Joao Pontel", dto.nome());
        assertEquals("joao@email.com", dto.email());
    }

    @Test
    @DisplayName("Deve retornar detalhes do usuário por ID")
    void visualizarUsuario_deveRetornarDetalheDTO() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioDetalheDTO dto = service.visualizarUsuario(1L);

        // Assert
        assertEquals(1L, dto.id());
        assertEquals("Joao Pontel", dto.nome());
        assertEquals("joao@email.com", dto.email());
        assertTrue(dto.ativo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar visualizar usuário não encontrado")
    void visualizarUsuario_deveLancarExcecaoSeNaoEncontrar() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.visualizarUsuario(1L));
        assertEquals("Usuario não encontrado com id: 1", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar nome e email do usuário")
    void atualizarUsuario_deveAtualizarCampos() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioAtualizarDTO dto = new UsuarioAtualizarDTO(1L, "Jose", "Silva", "jose@email.com");

        // Act
        service.atualizarUsuario(dto);

        // Assert
        assertEquals("Jose Silva", usuario.getNome());
        assertEquals("jose@email.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve ativar um usuário inativo")
    void alteradorUsuario_deveAtivarUsuario() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(1L);
        usuario.setAtivo(false);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        service.alteradorUsuario(1L, true);

        // Assert
        assertTrue(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve desativar um usuário ativo")
    void alteradorUsuario_deveDesativarUsuario() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(1L);
        usuario.setAtivo(true);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        service.alteradorUsuario(1L, false);

        // Assert
        assertFalse(usuario.isAtivo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar usuário não existente")
    void atualizarUsuario_usuarioNaoExistente_deveLancarExcecao() {
        // Arrange
        UsuarioAtualizarDTO dto = new UsuarioAtualizarDTO(99L, "Teste", "Mutacao", "teste@email.com");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.atualizarUsuario(dto));
        assertEquals("Usuario não encontrado com id: 99", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar status de usuário não existente")
    void alteradorUsuario_usuarioNaoExistente_deveLancarExcecao() {
        // Arrange
        when(repository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.alteradorUsuario(100L, true));
        assertEquals("Usuario não encontrado com id: 100", ex.getMessage());
    }

    @Test
    @DisplayName("Deve salvar usuário mesmo com nome e sobrenome vazios")
    void cadastarUsuario_nomeVazio_deveSalvarMesmoAssim() {
        // Arrange
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO("", "", "email@email.com", "senha");

        // Act
        Usuario usuario = service.cadastarUsuario(dto);

        // Assert
        assertNotNull(usuario);
        assertEquals(" ", usuario.getNome());
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve retornar página vazia ao listar usuários sem resultados")
    void listarPaginado_listaVazia_deveRetornarPaginaVazia() {
        // Arrange
        Page<Usuario> emptyPage = new PageImpl<>(List.of());
        when(repository.findAllByAtivoTrue(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<?> result = service.listarPaginado(Pageable.unpaged());

        // Assert
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("Não deve alterar usuário se o status for o mesmo (ativo)")
    void alteradorUsuario_mesmoStatusAtivoNaoDeveMudar() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(1L);
        usuario.setAtivo(true);
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        service.alteradorUsuario(1L, true);

        // Assert
        assertTrue(usuario.isAtivo());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve alterar usuário se o status for o mesmo (inativo)")
    void alteradorUsuario_mesmoStatusInativoNaoDeveMudar() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(3L);
        usuario.setAtivo(false);
        when(repository.findById(3L)).thenReturn(Optional.of(usuario));

        // Act
        service.alteradorUsuario(3L, false);

        // Assert
        assertFalse(usuario.isAtivo());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve inverter o status do usuário com sucesso")
    void alteradorUsuario_inverteStatus_deveFuncionar() {
        // Arrange
        Usuario usuario = new Usuario("Joao", "Pontel", "joao@email.com", "senha");
        usuario.setId(2L);
        usuario.setAtivo(false);
        when(repository.findById(2L)).thenReturn(Optional.of(usuario));

        // Act
        service.alteradorUsuario(2L, true);

        // Assert
        assertTrue(usuario.isAtivo());
    }
}