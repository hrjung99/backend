package swyp.swyp6_team7.notice.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import swyp.swyp6_team7.config.DataConfig;
import swyp.swyp6_team7.notice.entity.Notice;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import(DataConfig.class)
@DataJpaTest
public class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지 저장 테스트")
    public void testSaveNotice() {
        // Given
        Notice notice = new Notice();
        notice.setTitle("Test Notice");
        notice.setContent("This is a test notice.");

        // When
        Notice savedNotice = noticeRepository.save(notice);

        // Then
        assertThat(savedNotice).isNotNull();
        assertThat(savedNotice.getNoticeId()).isNotNull();
        assertThat(savedNotice.getTitle()).isEqualTo("Test Notice");
        assertThat(savedNotice.getContent()).isEqualTo("This is a test notice.");
    }

    @Test
    @DisplayName("공지 조회 테스트")
    public void testFindById() {
        // Given
        Notice notice = new Notice();
        notice.setTitle("Find Notice");
        notice.setContent("Find this notice.");
        Notice savedNotice = noticeRepository.save(notice);

        // When
        Optional<Notice> foundNotice = noticeRepository.findById(savedNotice.getNoticeId());

        // Then
        assertThat(foundNotice).isPresent();
        assertThat(foundNotice.get().getTitle()).isEqualTo("Find Notice");
    }

    @Test
    @DisplayName("공지 삭제 테스트")
    public void testDeleteNotice() {
        // Given
        Notice notice = new Notice();
        notice.setTitle("Delete Notice");
        notice.setContent("Delete this notice.");
        Notice savedNotice = noticeRepository.save(notice);

        // When
        noticeRepository.delete(savedNotice);
        Optional<Notice> deletedNotice = noticeRepository.findById(savedNotice.getNoticeId());

        // Then
        assertThat(deletedNotice).isNotPresent();
    }
}
