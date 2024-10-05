package swyp.swyp6_team7.image.s3;

public class FileDownloadFailedException extends RuntimeException {
    public FileDownloadFailedException() {
    }

    public FileDownloadFailedException(String message) {
        super(message);
    }
}
