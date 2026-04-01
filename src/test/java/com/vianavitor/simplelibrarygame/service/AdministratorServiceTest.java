package com.vianavitor.simplelibrarygame.service;

import com.vianavitor.simplelibrarygame.model.Administrator;
import com.vianavitor.simplelibrarygame.repository.AdministratorRepository;
import com.vianavitor.simplelibrarygame.service.utils.BaseServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

public class AdministratorServiceTest extends BaseServiceTest {

    @Mock
    private AdministratorRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AdministratorService service;

    @Test
    public void testLogin() {
        Administrator adm = new Administrator("adm_1", "passwd");
        adm.setId(1L);
//        adm.setActive(false);

        String username = "adm_1";
        String password = "passwd";
        LocalDate lastLoginAtBeginning = adm.getLastLogin();
        boolean validPassword = password.equals("passwd");

        Mockito.when(encoder.matches(password, "passwd"))
                .thenReturn(validPassword);

        Mockito.when(repository.findByUsername("adm_1"))
                .thenReturn(Optional.of(adm));

        Mockito.when(repository.save(adm))
                .thenReturn(adm);

        Long id = service.login(username, password);

        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(1L);
        assertThat(lastLoginAtBeginning).isNotEqualTo(adm.getLastLogin());
        Mockito.verify(encoder, Mockito.times(1)).matches(password, "passwd");
        Mockito.verify(repository, Mockito.times(1)).findByUsername("adm_1");
        Mockito.verify(repository, Mockito.times(1)).save(adm);
    }
}
