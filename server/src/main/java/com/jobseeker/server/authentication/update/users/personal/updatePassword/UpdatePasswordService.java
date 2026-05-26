package com.jobseeker.server.authentication.update.users.personal.updatePassword;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Tokens;
import com.jobseeker.server.models.Users;

@Service
public class UpdatePasswordService {
    private final UpdatePasswordInterface updatePasswordInterface;
    private final UserInterface userInterface;
    private final PasswordEncoder passwordEncoder;

    public UpdatePasswordService(UpdatePasswordInterface updatePasswordInterface,
            UserInterface userInterface,
            PasswordEncoder passwordEncoder) {
        this.updatePasswordInterface = updatePasswordInterface;
        this.userInterface = userInterface;
        this.passwordEncoder = passwordEncoder;
    }

    public String updatePassword(String token, UpdatePasswordValidation updatePasswordValidation) {
        try {
            // 1. Validate token exists and is active
            Tokens tokenObj = updatePasswordInterface.findByValue(token);
            if (tokenObj == null || !Boolean.TRUE.equals(tokenObj.getIsValid())) {
                return "Invalid or expired token";
            }

            // 2. Get user from token
            Users user = tokenObj.getUser();
            if (user == null) {
                return "User not found";
            }

            // 3. Verify old password matches
            if (!passwordEncoder.matches(updatePasswordValidation.oldPassword(), user.getPassword())) {
                return "Incorrect current password";
            }

            // 4. Check that new password is different from old password
            if (passwordEncoder.matches(updatePasswordValidation.newPassword(), user.getPassword())) {
                return "New password cannot be the same as old password";
            }

            // 5. Hash and update the new password
            String encodedNewPassword = passwordEncoder.encode(updatePasswordValidation.newPassword());
            user.setPassword(encodedNewPassword);

            // 6. Save updated user
            userInterface.save(user);

            return "Password updated successfully";

        } catch (Exception e) {
            return "Password update failed: " + e.getMessage();
        }
    }
}