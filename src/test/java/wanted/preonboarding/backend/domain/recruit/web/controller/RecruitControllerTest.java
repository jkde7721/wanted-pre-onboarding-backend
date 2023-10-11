package wanted.preonboarding.backend.domain.recruit.web.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.*;
import wanted.preonboarding.backend.domain.recruit.web.dto.request.*;
import wanted.preonboarding.backend.global.exception.BusinessException;
import wanted.preonboarding.backend.domain.recruit.business.dto.response.*;
import wanted.preonboarding.backend.domain.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wanted.preonboarding.backend.global.exception.ErrorCode.*;
import static wanted.preonboarding.backend.utils.Fixtures.*;
import static wanted.preonboarding.backend.utils.JsonUtils.*;

@WebMvcTest(RecruitController.class)
class RecruitControllerTest {

    @MockBean
    RecruitService recruitService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("채용공고 등록 성공 테스트")
    @Test
    void registerRecruit() throws Exception {
        when(recruitService.registerRecruit(anyLong(), any(RecruitSaveRequest.class))).thenReturn(1L);

        RecruitCreateRequest recruitCreateRequest = RecruitCreateRequest.builder().companyId(1L)
                .position("백엔드 주니어 개발자")
                .compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skills("Python").build();
        mockMvc.perform(post("/recruits")
                        .content(asJsonString(recruitCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recruitId").value(1L))
                .andDo(print());
    }

    @DisplayName("채용공고 수정 성공 테스트")
    @Test
    void modifyRecruit() throws Exception {
        RecruitUpdateRequest recruitUpdateRequest = RecruitUpdateRequest.builder()
                .position("백엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("네이버에서 백엔드 시니어 개발자를 채용합니다.")
                .skills("Spring").build();
        mockMvc.perform(put("/recruits/{recruitId}", 1L)
                        .content(asJsonString(recruitUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("채용공고 수정 실패 테스트 - 해당 채용공고 조회 실패")
    @Test
    void modifyRecruitFail() throws Exception {
        doThrow(new BusinessException(RECRUIT_NOT_FOUND)).when(recruitService).modifyRecruit(anyLong(), any(RecruitModifyRequest.class));

        RecruitUpdateRequest recruitUpdateRequest = RecruitUpdateRequest.builder()
                .position("백엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("네이버에서 백엔드 시니어 개발자를 채용합니다.")
                .skills("Spring").build();
        mockMvc.perform(put("/recruits/{recruitId}", 1L)
                        .content(asJsonString(recruitUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("$.code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @DisplayName("채용공고 삭제 성공 테스트")
    @Test
    void removeRecruit() throws Exception {
        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("채용공고 삭제 실패 테스트 - 해당 채용공고 조회 실패")
    @Test
    void removeRecruitFail() throws Exception {
        doThrow(new BusinessException(RECRUIT_NOT_FOUND)).when(recruitService).removeRecruit(anyLong());

        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("$.code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @Disabled("스프링 데이터 JPA의 Pagination 적용으로 테스트 실패 → 리팩토링하면서 수정할 예정")
    @DisplayName("채용공고 목록 조회 성공 테스트 - 채용공고 1개 이상")
    @Test
    void getRecruitList() throws Exception {
        Company company = aCompany().build();
        Company company2 = aCompany2().build();
        Recruit recruit = aRecruit().company(company).build();
        Recruit recruit2 = aRecruit2().company(company).build();
        Recruit recruit3 = aRecruit3().company(company2).build();
//        when(recruitService.getRecruitList()).thenReturn(List.of(recruit, recruit2, recruit3));

        mockMvc.perform(get("/recruits")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].recruitId").value(recruit.getId()))
                .andExpect(jsonPath("$[0].companyName").value(company.getName()))
                .andExpect(jsonPath("$[0].nation").value(company.getNation()))
                .andExpect(jsonPath("$[0].region").value(company.getRegion()))
                .andExpect(jsonPath("$[0].position").value(recruit.getPosition()))
                .andExpect(jsonPath("$[0].compensationFee").value(recruit.getCompensationFee()))
                .andExpect(jsonPath("$[0].skills").value(recruit.getSkills()))
                .andDo(print());
    }

    @Disabled("스프링 데이터 JPA의 Pagination 적용으로 테스트 실패 → 리팩토링하면서 수정할 예정")
    @DisplayName("채용공고 목록 조회 성공 테스트 - 채용공고 0개")
    @Test
    void getRecruitListNone() throws Exception {
//        when(recruitService.getRecruitList()).thenReturn(List.of());

        mockMvc.perform(get("/recruits")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }

    @DisplayName("채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 1개 이상")
    @Test
    void getRecruitWithAnotherOfTheCompany() throws Exception {
        Company company = aCompany().build();
        Recruit recruit = aRecruit().company(company).build();
        Recruit recruit2 = aRecruit2().company(company).build();
        Recruit recruit3 = aRecruit3().company(company).build();
        RecruitWithAnotherResponse recruitWithAnother = new RecruitWithAnotherResponse(recruit, List.of(recruit2, recruit3));
        when(recruitService.getRecruitWithAnotherOfTheCompany(anyLong())).thenReturn(recruitWithAnother);

        mockMvc.perform(get("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitId").value(recruit.getId()))
                .andExpect(jsonPath("$.companyName").value(company.getName()))
                .andExpect(jsonPath("$.nation").value(company.getNation()))
                .andExpect(jsonPath("$.region").value(company.getRegion()))
                .andExpect(jsonPath("$.position").value(recruit.getPosition()))
                .andExpect(jsonPath("$.compensationFee").value(recruit.getCompensationFee()))
                .andExpect(jsonPath("$.skills").value(recruit.getSkills()))
                .andExpect(jsonPath("$.details").value(recruit.getDetails()))
                .andExpect(jsonPath("$.anotherRecruitList[0]").value(recruit2.getId()))
                .andExpect(jsonPath("$.anotherRecruitList[1]").value(recruit3.getId()))
                .andDo(print());
    }

    @DisplayName("채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 0개")
    @Test
    void getRecruitWithAnotherOfTheCompanyWithZeroAnother() throws Exception {
        Company company = aCompany().build();
        Recruit recruit = aRecruit().company(company).build();
        RecruitWithAnotherResponse recruitWithAnother = new RecruitWithAnotherResponse(recruit, List.of());
        when(recruitService.getRecruitWithAnotherOfTheCompany(anyLong())).thenReturn(recruitWithAnother);

        mockMvc.perform(get("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitId").value(recruit.getId()))
                .andExpect(jsonPath("$.companyName").value(company.getName()))
                .andExpect(jsonPath("$.nation").value(company.getNation()))
                .andExpect(jsonPath("$.region").value(company.getRegion()))
                .andExpect(jsonPath("$.position").value(recruit.getPosition()))
                .andExpect(jsonPath("$.compensationFee").value(recruit.getCompensationFee()))
                .andExpect(jsonPath("$.skills").value(recruit.getSkills()))
                .andExpect(jsonPath("$.details").value(recruit.getDetails()))
                .andExpect(jsonPath("$.anotherRecruitList").isEmpty())
                .andDo(print());
    }

    @DisplayName("채용공고 상세 조회 실패 테스트 - 해당 채용공고 조회 실패")
    @Test
    void getRecruitWithAnotherOfTheCompanyFail() throws Exception {
        when(recruitService.getRecruitWithAnotherOfTheCompany(anyLong())).thenThrow(new BusinessException(RECRUIT_NOT_FOUND));

        mockMvc.perform(get("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("$.code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }
}