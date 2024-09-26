package swyp.swyp6_team7.profile.dto;

import lombok.Getter;
import lombok.Setter;
import swyp.swyp6_team7.member.entity.Users;
import swyp.swyp6_team7.profile.entity.UserProfile;
import swyp.swyp6_team7.tag.domain.Tag;
import swyp.swyp6_team7.tag.domain.UserTagPreference;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProfileViewResponse {
    private String email;
    private String name;
    private String gender;
    private String ageGroup;
    private String proIntroduce;
    private String[] preferredTags;

    public ProfileViewResponse(Users user, UserProfile userProfile) {
        this.email = user.getUserEmail();
        this.name = user.getUserName();
        this.gender = user.getUserGender().name();
        this.ageGroup = user.getUserAgeGroup().getValue();
        this.proIntroduce = userProfile.getProIntroduce();

        // 태그 목록을 가져와서 배열로 변환
        Set<Tag> tagSet = user.getPreferredTags();
        if (tagSet != null && !tagSet.isEmpty()) {
            this.preferredTags = tagSet.stream()
                    .map(Tag::getName)
                    .toArray(String[]::new);
        } else {
            this.preferredTags = new String[0]; // 태그가 없을 경우 빈 배열
        }
    }


}
