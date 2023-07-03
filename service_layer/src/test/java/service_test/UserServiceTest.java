package service_test;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Provider;
import com.epam.esm.entity.User;
import com.epam.esm.repository.ProviderRepository;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service_impl.UserServiceImpl;
import com.epam.esm.util_service.ProviderName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {UserServiceImpl.class})
public class UserServiceTest extends Mockito {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository     userRepository;
    @MockBean
    private RoleRepository     roleRepository;
    @MockBean
    private ProviderRepository providerRepository;
    @MockBean
    private PasswordEncoder    passwordEncoder;

    User user1 = User.builder().id(1L).email("1").password("1").username("1").firstName("1").lastName("1")
            .provider(Provider.builder().name(ProviderName.LOCAL.name()).build()).build();

    UserDTO user1DTO =
            UserDTO.builder().email("1").username("1").firstName("1").lastName("1").provider(ProviderName.LOCAL).build();

    @Test
    public void testGetTagSuccess() {

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        assertEquals(Optional.of(user1DTO), userService.getUser(user1DTO.getEmail()));
        verify(userRepository).findByEmail(user1.getEmail());
    }

    @Test
    public void testGetTagFail() {

        when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.empty());

        assertTrue(userService.getUser(user1DTO.getEmail()).isEmpty());
        verify(userRepository).findByEmail(user1DTO.getEmail());
    }
}
