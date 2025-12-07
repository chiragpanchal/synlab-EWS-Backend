package com.ewsv3.ews.auth.config;

import com.ewsv3.ews.auth.dto.User;
import com.ewsv3.ews.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create a test user if no users exist
        if (userRepository.count() == 0) {
            User testUser = new User();
            testUser.setUserId(1L);
            testUser.setUserName("testuser");
            testUser.setPassword(passwordEncoder.encode("password123")); // This will create BCrypt hash
            testUser.setRole("ADMIN");
            testUser.setUserType("ADMIN");
            testUser.setEmployeeId(1001L);
            testUser.setEnterpriseId(1L);
            testUser.setStartDate(new Date());
            // endDate is null for active users
            
            userRepository.save(testUser);
            // System.out.println("Test user created: username='testuser', password='password123'");
            
            // Create user with ID 1059 and BCrypt password
            User user1059 = new User();
            user1059.setUserId(1059L);
            user1059.setUserName("1059");
            user1059.setPassword("$2a$10$SVFGvIGTRIC6Q.coVQye1Olkn4DQm3QDqYGEtk8yhN.6xdTnCE80y"); // Your BCrypt password
            user1059.setRole("USER");
            user1059.setUserType("USER");
            user1059.setEmployeeId(1059L);
            user1059.setEnterpriseId(1L);
            user1059.setStartDate(new Date());
            
            userRepository.save(user1059);
            // System.out.println("User 1059 created with BCrypt password");
        }
    }
}
