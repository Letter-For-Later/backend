package org.example.letter.domain.user.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.letter.global.domain.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID uid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    // 기본 생성자에서 UUID 자동 생성
    public User(String email, String nickname, LoginType loginType) {
        this.uid = UUID.randomUUID();
        this.email = email;
        this.nickname = nickname;
        this.loginType = loginType;
    }

    // 생성자 오버로드 (UUID 지정 가능)
    public User(UUID uid, String email, String nickname, LoginType loginType) {
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
        this.loginType = loginType;
    }
}
