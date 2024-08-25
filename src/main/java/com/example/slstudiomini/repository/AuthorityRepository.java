package com.example.slstudiomini.repository;

import java.util.Optional; // Optionalクラスをインポート（null値を扱うためのコンテナ）

import org.springframework.data.jpa.repository.JpaRepository; // JpaRepositoryをインポート（CRUD操作をサポート）
import org.springframework.stereotype.Repository; // Repositoryアノテーションをインポート（リポジトリクラスを示す）

import com.example.slstudiomini.model.Authority; // Authorityエンティティをインポート

// Repositoryアノテーションを付けて、このインターフェースがリポジトリとして機能することをSpringに示す
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    // JpaRepository<Authority, Long>を継承して、Authorityエンティティに対するCRUD操作を提供
    // Longはエンティティの主キーの型

    // ユーザーIDに基づいてAuthorityエンティティを検索するためのカスタムクエリメソッド
    Optional<Authority> findByUserId(Long userId);
    // Optional<Authority>を返し、検索結果が存在しない場合でもnullを返さないようにする
    // userIdを引数に取ることで、特定のユーザーに関連する権限を取得できる
}
