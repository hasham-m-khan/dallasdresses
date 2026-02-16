package com.dallasdresses.entities;

import com.dallasdresses.entities.enums.Hasher;
import com.dallasdresses.entities.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credentials")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_id", nullable = false, length = 50)
    private Provider providerId;

    @Column(name = "provider_key", nullable = false, unique = true)
    private String providerKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "hasher", length = 50)
    private Hasher hasher;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "password_salt")
    private String passwordSalt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credential)) return false;
        Credential that = (Credential) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
