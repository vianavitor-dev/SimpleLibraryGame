package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Classroom;
import com.vianavitor.simplelibrarygame.model.Librarian;
import com.vianavitor.simplelibrarygame.repository.ClassroomRepository;
import com.vianavitor.simplelibrarygame.repository.LibrarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class LibrarianServiceTest {    
    
    @Mock
    private LibrarianRepository repository;

    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private LibrarianService service;

    private Librarian librarian;

    @BeforeEach
    public void init() {
        librarian = new Librarian("librarian_1", "passwd");
        librarian.setName("librarian_name");
    }

    @Test
    void testRegister() {
        Mockito.when(repository.existsByUsername("librarian_1"))
                .thenReturn(false);

        Mockito.when(encoder.encode("passwd"))
                .thenReturn("passwd_hash");

        Mockito.when(repository.save(Mockito.any(Librarian.class)))
                .thenAnswer(x -> {
                    librarian.setId(1L);

                    return librarian;
                });

        Librarian result = service.register(librarian);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPassword()).isEqualTo("passwd_hash");

        Mockito.verify(repository, Mockito.times(1)).existsByUsername("librarian_1");
        Mockito.verify(repository, Mockito.times(1)).save(librarian);
        Mockito.verify(encoder, Mockito.times(1)).encode("passwd");
    }

    @Test
    void testLogin() {
        librarian = new Librarian("librarian_1", "passwd");
        librarian.setId(1L);
//        librarian.setActive(false);

        String username = "librarian_1";
        String password = "123";
        LocalDate lastLoginAtBeginning = librarian.getLastLogin();
        boolean validPassword = password.equals("123");

        Mockito.when(encoder.matches(password, "passwd"))
                .thenReturn(validPassword);

        Mockito.when(repository.findByUsername("librarian_1"))
                .thenReturn(Optional.of(librarian));

        Mockito.when(repository.save(librarian))
                .thenReturn(librarian);

        Long id = service.login(username, password);

        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(1L);
        assertThat(lastLoginAtBeginning).isNotEqualTo(librarian.getLastLogin());

        Mockito.verify(encoder, Mockito.times(1)).matches(password, "passwd");
        Mockito.verify(repository, Mockito.times(1)).findByUsername("librarian_1");
        Mockito.verify(repository, Mockito.times(1)).save(librarian);
    }

    @Test
    void getAll() {
        List<Librarian> list = new ArrayList<>();
        list.add(librarian);

        Mockito.when(repository.findAll())
                .thenReturn(list);

        List<Librarian> result = service.getAll();

        assertThat(result).isNotNull();
        assertThat(result).size().isEqualTo(1);
        assertThat(result).contains(librarian);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void deactivate() {
        Long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(librarian));

        service.deactivate(id);

        assertThat(librarian).isNotNull();
        assertThat(librarian.isActive()).isFalse();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    void activate() {
        Long id = 1L;

        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(librarian));

        service.activate(id);

        assertThat(librarian).isNotNull();
        assertThat(librarian.isActive()).isTrue();

        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }
}
