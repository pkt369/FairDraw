package birdjun.profairmanager.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER", "사용자", "USER"),
    MANAGER("ROLE_MANAGER", "선생님", "MANAGER"),
    ADMIN("ROLE_ADMIN", "관리자", "ADMIN"),
    MASTER("ROLE_MASTER", "마스터", "MASTER");

    private final String key;
    private final String korean;
    private final String name;
}
