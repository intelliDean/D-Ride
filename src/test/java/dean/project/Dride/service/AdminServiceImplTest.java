package dean.project.Dride.service;

import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.data.dto.response.api_response.GlobalApiResponse;
import dean.project.Dride.services.admin_service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
class AdminServiceImplTest {
     @Autowired
    private AdminService adminService;
    private InviteAdminRequest inviteAdminRequests;

    void setUp() {

    }

    @Test
    void sendInviteRequests() {
        inviteAdminRequests = new InviteAdminRequest("juluvupeb.qapugufod@jollyfree.com", "Michael Dean");
        GlobalApiResponse response = adminService.sendInviteRequests(inviteAdminRequests);
        assertThat(response).isNotNull();
    }

    @Test
    void adminDetails() {

    }

    @Test
    void getAdminById() {
    }

    @Test
    void saveAdmin() {
    }

    @Test
    void getAllAdmins() {
    }

    @Test
    void getAdminByEmail() {
    }

    @Test
    void getAdminByUserId() {
    }
}