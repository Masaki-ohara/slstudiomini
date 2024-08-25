package com.example.slstudiomini.repository;

// Spring Data JPA の JpaRepository をインポート
import org.springframework.data.jpa.repository.JpaRepository;
// このクラスが Spring のリポジトリコンポーネントであることを示すアノテーション
import org.springframework.stereotype.Repository;

// 自作の User エンティティをインポート
import com.example.slstudiomini.model.User;

@Repository // このインターフェースが Spring のリポジトリ層として機能することを示す
public interface UserRepository extends JpaRepository<User, Long> {
    // User エンティティを操作するためのリポジトリインターフェース
    // JpaRepository を継承することで、基本的な CRUD 操作が自動的に提供される

    User findByUsername(String username);
    // username フィールドに基づいて User エンティティを検索するためのメソッド
    // メソッド名に基づいて Spring Data JPA が自動的にクエリを生成する
    // これにより、指定された username を持つユーザーが返される
}
