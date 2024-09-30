package swyp.swyp6_team7.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessageType {

    TRAVEL_ENROLL_HOST("여행 신청 알림") {
        public String getContent(String travelTitle) {
            return String.format("[%s]에 참가 신청자가 있어요. 알림을 눌러 확인해보세요.", travelTitle);
        }
    },
    TRAVEL_ENROLL("참가 신청 알림") {
        public String getContent(String travelTitle) {
            return String.format("[%s]에 참가 신청이 완료되었어요. 주최자가 참가를 확정하면 알려드릴게요.", travelTitle);
        }
    },

    TRAVEL_ACCEPT("참가 확정 알림") {
        public String getContent(String travelTitle) {
            return String.format("[%s]에 참가가 확정되었어요. 멤버 댓글을 통해 인사를 나눠보세요.", travelTitle);
        }
    },

    TRAVEL_REJECT("참가 거절 알림") {
        public String getContent(String travelTitle) {
            return String.format("[%s]에 참가가 아쉽게도 거절되었어요. 다른 여행을 찾아볼까요?", travelTitle);
        }
    };

    private final String title;
    public abstract String getContent(String travelTitle);
}
