package swyp.swyp6_team7.member.entity;

public enum SocialProvider {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google");

    private final String providerName;

    SocialProvider(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }

    public static SocialProvider fromString(String providerName) {
        for (SocialProvider provider : SocialProvider.values()) {
            if (provider.getProviderName().equalsIgnoreCase(providerName)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown social provider: " + providerName);
    }
}
