package swyp.swyp6_team7.image.s3;

public enum FileFolder {
    PROFILE("profile/"),
    COMMUNITY("community/");

    private final String folderPath;

    // enum 생성자
    FileFolder(String folderPath) {
        this.folderPath = folderPath;
    }

    // 폴더 경로를 반환하는 메소드
    public String getFolderPath() {
        return folderPath;
    }

    // 입력된 값이 PROFILE 또는 COMMUNITY에 해당하는지 체크
    public static FileFolder from(String value) {
        for (FileFolder folder : FileFolder.values()) {
            if (folder.name().equalsIgnoreCase(value)) {
                return folder;
            }
        }
        throw new IllegalArgumentException("Invalid folder type: " + value);
    }}
