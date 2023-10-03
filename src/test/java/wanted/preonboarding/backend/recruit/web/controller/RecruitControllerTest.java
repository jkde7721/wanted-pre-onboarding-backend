package wanted.preonboarding.backend.recruit.web.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.recruit.business.dto.request.RecruitSaveRequest;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.web.dto.request.RecruitCreateRequest;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wanted.preonboarding.backend.utils.JsonUtils.asJsonString;

@WebMvcTest(RecruitController.class)
class RecruitControllerTest {

    @MockBean
    RecruitService recruitService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void registerRecruit() throws Exception {
        Mockito.when(recruitService.registerRecruit(anyLong(), any(RecruitSaveRequest.class)))
                .thenReturn(1L);

        RecruitCreateRequest recruitCreateRequest = RecruitCreateRequest.builder().companyId(1L)
                .position("백엔드 주니어 개발자")
                .compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skills("Python").build();
        mockMvc.perform(post("/recruits")
                        .content(asJsonString(recruitCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recruitId").value(1L))
                .andDo(print());
    }
}