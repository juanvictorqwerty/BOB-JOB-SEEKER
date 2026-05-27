package com.jobseeker.server.authentication.update.users.changeRank;

import org.springframework.stereotype.Service;

import com.jobseeker.server.models.Users;

@Service
public class ChangeRankService {
    private final ChangeRankInterface changeRankInterface;

    public ChangeRankService(ChangeRankInterface changeRankInterface) {
        this.changeRankInterface = changeRankInterface;
    }

    public String changeRank(ChangeRankValidation changeRankValidation) {
        try {
            Users user = changeRankInterface.findByEmail(changeRankValidation.userEmail());
            if (user == null) {
                return "User not found";
            }
            user.setUserRank(changeRankValidation.newRank());
            changeRankInterface.save(user);
            return "Rank changed successfully";
        } catch (Exception e) {
            return "Rank change failed: " + e.getMessage();
        }
    }
}
