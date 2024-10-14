import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerapp.app.controller.PlayerController;
import com.soccerapp.app.dto.PlayerDto;
import com.soccerapp.app.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PlayerController.class)
@AutoConfigureMockMvc
@Import(TestConfig.class)
class PlayerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPlayer() throws Exception {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setAvailability("Available");
        playerDto.setPosition("Forward");
        playerDto.setSkillLevel("Advanced");

        when(playerService.createPlayer(any(PlayerDto.class))).thenReturn(playerDto);

        mockMvc.perform(post("/players/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("redirect:/players"));
    }

    @Test
    void listPlayers() throws Exception {
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(view().name("players/list"));
    }

    // Add more tests for other endpoints...
}

