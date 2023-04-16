package dean.project.Dride.service;

import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.services.admin_service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;
    private InviteAdminRequest inviteAdminRequests;
    @BeforeEach
    void setUp() {
        inviteAdminRequests = new InviteAdminRequest("bonel60110@orgria.com", "test_name");
    }

//    @Test
//    void sendInviteRequestsTest() {
//        var response = adminService.sendInviteRequests(inviteAdminRequests);
//        assertThat(response).isNotNull();
//    }
}