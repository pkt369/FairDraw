package birdjun.profairmanager.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DisabledType {
    None("none", "비장애"),
    INTELLECTUAL("intellectual", "지적장애"),
    AUTISM("autism", "자폐"),
    LANGUAGE("language", "언어장애"),
    DEVELOPMENTAL_DELAY("developmental delay", "발달장애");

    private final String englishName;
    private final String koreanName;
}
