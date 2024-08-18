package com.example.slstudiomini.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // アカウント
    @Column(nullable = false, unique = true)
    private String username;

    // パスワード
    @Column(nullable = false)
    private String password;

     // 有効・無効フラグ
    @Column(nullable = false)
    private boolean enabled;

    // 多対多のリレーションを定義するアノテーション
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        // 中å間テーブルの名前を指定
        name = "user_authority",
        // 現在のエンティティ（Userクラス）のIDを格納するカラムを指定
        joinColumns = @JoinColumn(name = "user_id"),
        // 対応するエンティティ（Authorityクラス）のIDを格納するカラムを指定
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )

    private Set<Authority> authorities;
    // ユーザーが持つ権限(Authority)の集合を保持するフィールド。
    // Setを使用することで、重複する権限がないように保証します。
    // Authorityクラスのオブジェクトのセットが、このユーザーに関連付けられます。
    // これにより、ユーザーが持つ複数の権限を管理できます。    

    public User() {
    }

    public User(Long id, String username, string password, boolean enabled,  Set<Authority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}