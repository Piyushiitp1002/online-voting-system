package auth;
import services.VoterService;
import services.AdminService;

public class AuthService {

    private VoterService voterService;
    private AdminService adminService;

    
    public AuthService(VoterService vs, AdminService as) {
        this.voterService = vs;
        this.adminService = as;
    }

    
    public boolean authenticateVoter(String id, String password) {
        return voterService.login(id, password);
    }

    
    public boolean authenticateAdmin(String id, String password) {
        return adminService.login(id, password);
    }
}

