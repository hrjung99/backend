package swyp.swyp6_team7.notice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import swyp.swyp6_team7.notice.dto.NoticeRequestDto;
import swyp.swyp6_team7.notice.dto.NoticeResponseDto;
import swyp.swyp6_team7.notice.entity.Notice;
import swyp.swyp6_team7.notice.repository.NoticeRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NoticeServiceTest {

    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("전체 공지사항 조회 테스트")
    public void testGetAllNotices() {
        // Given
        Notice notice1 = new Notice(1L, "Notice 1", "Content 1", LocalDateTime.now(), 0);
        Notice notice2 = new Notice(2L, "Notice 2", "Content 2", LocalDateTime.now(), 0);
        List<Notice> notices = Arrays.asList(notice1, notice2);

        when(noticeRepository.findAll()).thenReturn(notices);

        // When
        List<NoticeResponseDto> result = noticeService.getAllNotices();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Notice 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Notice 2");
    }

    @Test
    @DisplayName("ID로 공지사항 조회 테스트")
    public void testGetNoticeById() {
        // Given
        Notice notice = new Notice(1L, "Notice 1", "Content 1", LocalDateTime.now(), 0);
        when(noticeRepository.findById(1L)).thenReturn(Optional.of(notice));

        // When
        NoticeResponseDto result = noticeService.getNoticeById(1L);

        // Then
        assertThat(result.getTitle()).isEqualTo("Notice 1");
        assertThat(result.getContent()).isEqualTo("Content 1");
    }

    @Test
    @DisplayName("ID로 공지사항 조회 실패 테스트")
    public void testGetNoticeById_NotFound() {
        // Given
        when(noticeRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            // When
            noticeService.getNoticeById(1L);
        });
    }

    @Test
    @DisplayName("공지사항 생성 테스트")
    public void testCreateNotice() {
        // Given
        NoticeRequestDto noticeRequest = new NoticeRequestDto("New Notice", "New Content");
        Notice savedNotice = new Notice(1L, "New Notice", "New Content", LocalDateTime.now(), 0);

        when(noticeRepository.save(any(Notice.class))).thenReturn(savedNotice);

        // When
        NoticeResponseDto result = noticeService.createNotice(noticeRequest);

        // Then
        assertThat(result.getTitle()).isEqualTo("New Notice");
        assertThat(result.getContent()).isEqualTo("New Content");
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    @DisplayName("공지사항 수정 테스트")
    public void testUpdateNotice() {
        // Given
        Notice existingNotice = new Notice(1L, "Old Notice", "Old Content", LocalDateTime.now(), 0);
        NoticeRequestDto noticeRequest = new NoticeRequestDto("Updated Notice", "Updated Content");

        when(noticeRepository.findById(1L)).thenReturn(Optional.of(existingNotice));
        when(noticeRepository.save(any(Notice.class))).thenReturn(existingNotice);

        // When
        NoticeResponseDto result = noticeService.updateNotice(1L, noticeRequest);

        // Then
        assertThat(result.getTitle()).isEqualTo("Updated Notice");
        assertThat(result.getContent()).isEqualTo("Updated Content");
        verify(noticeRepository, times(1)).save(existingNotice);
    }

    @Test
    @DisplayName("공지사항 수정 실패 테스트 - 공지사항 미존재")
    public void testUpdateNotice_NotFound() {
        // Given
        NoticeRequestDto noticeRequest = new NoticeRequestDto("Updated Notice", "Updated Content");

        when(noticeRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            // When
            noticeService.updateNotice(1L, noticeRequest);
        });
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    public void testDeleteNotice() {
        // Given
        doNothing().when(noticeRepository).deleteById(1L);

        // When
        noticeService.deleteNotice(1L);

        // Then
        verify(noticeRepository, times(1)).deleteById(1L);
    }
}
