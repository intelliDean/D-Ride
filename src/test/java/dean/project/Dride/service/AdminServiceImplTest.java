package dean.project.Dride.service;

import dean.project.Dride.data.dto.request.InviteAdminRequest;
import dean.project.Dride.services.admin_service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Slf4j
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