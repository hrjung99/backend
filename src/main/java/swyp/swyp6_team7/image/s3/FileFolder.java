package swyp.swyp6_team7.image.s3;

public enum FileFolder {
    PROFILE("profile"),
    COMMUNITY("community");

    private final String folderName;

    FileFolder(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public static FileFolder from(String relatedType) {
        for (FileFolder folder : FileFolder.values()) {
            if (folder.folderName.equalsIgnoreCase(relatedType)) {
                return folder;
            }
        }
        throw new IllegalArgumentException("Invalid relatedType: " + relatedType);
    }
}
