package com.example.slstudiomini.service;

import java.util.Set; // Setクラスをインポート（重複しない要素の集まりを保持）
import java.util.stream.Collectors; // Collectorsクラスをインポート（ストリームの結果をコレクションに変換）

import org.springframework.beans.factory.annotation.Autowired; // Autowiredアノテーションをインポート（依存性注入）
import org.springframework.security.core.GrantedAuthority; // GrantedAuthorityインターフェースをインポート（ユーザーの権限を表す）
import org.springframework.security.core.authority.SimpleGrantedAuthority; // SimpleGrantedAuthorityクラスをインポート（単純な権限を持つクラス）
import org.springframework.security.core.userdetails.UserDetails; // UserDetailsインターフェースをインポート（Spring Securityで認証済みユーザーを表す）
import org.springframework.security.core.userdetails.UserDetailsService; // UserDetailsServiceインターフェースをインポート（ユーザー詳細サービスの提供）
import org.springframework.security.core.userdetails.UsernameNotFoundException; // UsernameNotFoundExceptionクラスをインポート（ユーザーが見つからなかった場合の例外）
import org.springframework.stereotype.Service; // Serviceアノテーションをインポート（サービスクラスとして定義）

import com.example.slstudiomini.model.User; // Userエンティティをインポート（カスタムユーザーエンティティ）
import com.example.slstudiomini.repository.AuthorityRepository; // AuthorityRepositoryをインポート（権限リポジトリ）
import com.example.slstudiomini.repository.UserRepository; // UserRepositoryをインポート（ユーザーリポジトリ）

// Serviceアノテーションを付けて、このクラスがサービス層のコンポーネントであることをSpringに示す
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // UserRepositoryを自動的に注入する
    @Autowired
    private UserRepository userRepository;

    // AuthorityRepositoryを自動的に注入する
    @Autowired
    private AuthorityRepository authorityRepository;

    // ユーザー名に基づいてユーザー詳細をロードするメソッド
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // UserRepositoryを使って、指定されたユーザー名に対応するUserエンティティを検索
        User user = userRepository.findByUsername(username);

        // ユーザーが見つからない場合、UsernameNotFoundExceptionをスロー
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // AuthorityRepositoryを使って、ユーザーに関連する権限を検索し、SimpleGrantedAuthorityオブジェクトに変換
        Set<GrantedAuthority> grantedAuthorities = authorityRepository.findByUserId(user.getId())
            .stream() // 権限のストリームを作成
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())) 
            // 各権限をSimpleGrantedAuthorityオブジェクトに変換
            .collect(Collectors.toSet()); // 権限をSetコレクションに集約

        // Spring Securityが提供するUserDetailsオブジェクトを返す
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), // ユーザー名
            user.getPassword(), // パスワード
            user.isEnabled(), // アカウントが有効かどうか
            true, // アカウントの有効期限が切れていないか（trueなら有効）
            true, // 資格情報の有効期限が切れていないか（trueなら有効）
            true, // アカウントがロックされていないか（trueならロックされていない）
            grantedAuthorities // ユーザーに付与された権限のセット
        );
    }
}
