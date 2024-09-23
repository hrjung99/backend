package swyp.swyp6_team7.travelImage.dto.response;

public class CommonResponse<T> {

    private boolean success; //요청 성공 여부
    private String message; //응답 메시지
    private T data; //응답 데이터

    //성공 응답 생성 메서드
    public static <T> CommonResponse<T> sucess(T data, String message){
        CommonResponse<T> response  = new CommonResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    public static <T> CommonResponse<T> fail(String message) {
        CommonResponse<T> response = new CommonResponse<>();
        response.success = false;
        response.message = message;
        response.data = null;

        return response;
    }
}
