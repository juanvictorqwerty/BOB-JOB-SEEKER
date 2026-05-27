package com.jobseeker.server.authentication.update.users.personal.updateEmail;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Tokens;
import com.jobseeker.server.models.Users;

@Service
public class ChangEmailService {
    private final ChangeEmailInterface changeEmailInterface;
    private final UserInterface userInterface;
    private final PasswordEncoder passwordEncoder;

    public ChangEmailService(ChangeEmailInterface changeEmailInterface,
            UserInterface userInterface,
            PasswordEncoder passwordEncoder) {
        this.changeEmailInterface = changeEmailInterface;
        this.userInterface = userInterface;
        this.passwordEncoder = passwordEncoder;
    }

    public String changeEmail(String token, ChangeEmailValidation changeEmailValidation) {
        try {
            // 1. Validate token exists and is active
            Tokens tokenObj = changeEmailInterface.findByValue(token);
            if (tokenObj == null || !Boolean.TRUE.equals(tokenObj.getIsValid())) {
                return "Invalid or expired token";
            }

            // 2. Get user from token
            Users user = tokenObj.getUser();
            if (user == null) {
                return "User not found";
            }

            // 3. Verify current password (security check)
            if (!passwordEncoder.matches(changeEmailValidation.currentPassword(), user.getPassword())) {
                return "Incorrect current password";
            }

            // 4. Check if new email is already taken by another user
            Users existingUser = userInterface.findByEmail(changeEmailValidation.newEmail());
            if (existingUser != null && !existingUser.getId().equals(user.getId())) {
                return "Email already in use by another account";
            }

            // 5. Update email
            user.setEmail(changeEmailValidation.newEmail());

            // FIX: Use userInterface.save() for a single Users entity
            userInterface.save(user);

            return "Email changed successfully";

        } catch (Exception e) {
            return "Email change failed: " + e.getMessage();
        }
    }
}