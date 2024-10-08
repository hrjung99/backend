package swyp.swyp6_team7.location.util;

public class KoreanCharDecomposer {
    // 한글 초성, 중성, 종성의 범위 정의
    private static final char BASE_CODE = 0xAC00;
    private static final char CHOSUNG_BASE = 0x1100;
    private static final char JUNGSUNG_BASE = 0x1161;
    private static final char JONGSUNG_BASE = 0x11A7;

    public static String decompose(char character) {
        int unicode = character - BASE_CODE;

        if (unicode < 0 || unicode > 11172) {
            return String.valueOf(character); // 한글이 아닐 경우 그대로 반환
        }

        // 초성 인덱스 계산
        int chosungIndex = unicode / (21 * 28);
        // 중성 인덱스 계산
        int jungsungIndex = (unicode % (21 * 28)) / 28;
        // 종성 인덱스 계산
        int jongsungIndex = unicode % 28;

        // 결과 문자열 생성 (초성, 중성, 종성 조합)
        StringBuilder result = new StringBuilder();
        result.append((char) (CHOSUNG_BASE + chosungIndex));
        result.append((char) (JUNGSUNG_BASE + jungsungIndex));
        if (jongsungIndex != 0) {
            result.append((char) (JONGSUNG_BASE + jongsungIndex));
        }

        return result.toString();
    }
}
