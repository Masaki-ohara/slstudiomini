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

認証・認可と利用
課題

0

認証・認可と利用
 ブックマーク
目次

認証・認可をサポートするSpring Security
開発前の確認と準備
SLStudioMiniの実装(ここからコピペ禁止!)
コースとレッスンのCRUD作成
ログインユーザーの作成
まとめ
WEBアプリケーションに必要な技術において、これまでの学習に加えこの認証・認可の機能を学べばかなりの範囲をカバーしたと言えるでしょう。

認証認可とはいわゆるログイン(認証)やログイン後のユーザーの立場によってアクセスできる機能を制御(認可)出来る機能です。

認証・認可なんて言うと小難しく聞こえますが大したことはやってません。

認証の例

認証機能はログインだけではなく、ログインしたユーザーによってデータを出し分ける必要があります。このSLスタジオ自体もあなたのログイン情報を元に学習の進捗等が保存されていますね。この判別は「ユーザーID」などによって行われます。

認可の例

お客さんの立場でばかりWEBサービスを利用していると気が付かないかもしれませんが、WEBサービスには大体管理画面が存在し、そこでデータのコントロールや顧客の管理を行っています。このSLスタジオも管理画面があり、そこからこの教材の登録を行っているのです。

この管理画面は誰でもアクセス出来るものではありません。

上の例で言うと「先生」は自分の成績なんて無いので、そもそも「生徒」と「先生」はそれぞれ違う機能にアクセスするのです。

この判別はユーザーIDで細かく制御するより「ロール(役割)」というデータを与える事で制御するのが一般的です。



これら認証・認可の機能を強力にサポートする機能もSpring Bootは備えていますのでまずはそれを学んでいきましょう。



認証・認可をサポートするSpring Security
さぁこれからSpring Securityの機能をマスターしよう！と言いたい所ですが、Spring Securityを使った認証・認可は何となく覚えて貰うだけで良いです。

認証や認可が行われているのはSpring Securityを使っているから

こういう設定が出来るんだな

程度で構いません。

一旦自分で書いて見る方がより説明は分かりやすいですが、この部分に関してはコピペでも何でも構いません。それほどにこの部分はアプリケーションとしては必須だけど学習としては”まだ”必要が無いという事です。

しかし一度コピペを許すと文章も読まない人がいます。何となく覚えるというのはソースコードの流れの話であって本書の説明部分に関しては非常に重要です。コピペして良い分コードの解説部分は集中して学んでください。

実際に開発現場にデビューしても、そうそう認証機能を作る機会はありません。

本レッスンで学んで欲しいのはSpring Securityで認証・認可機能を実装した「後」、そのログインデータやロールのデータを使ってデータや機能を切り分ける方法なのです。

Spring Securityを導入したアプリケーションの準備
ずっとbookmanagementsampleを修正していたので久しぶりに新規アプリケーションを作りましょう。

今回は今皆さんが使っているSLスタジオの様な学習サービスを例にしていきます。

イメージは付きますか？先生(ADMIN)がコースやレッスンを登録して生徒(USER)はそれを閲覧することが出来ます。USERとADMINはそれぞれお互いのページには行けません。



それでは新しいプロジェクト「slstudiomini」を作成してください。

依存ライブラリで「Spring Security」を導入します。



これを加えるとこれまでの依存ライブラリと合わせて５個選択されているはずです。



プロジェクトを作成したら一度build.gradleのファイルを確認しておきましょう。

build.gradle

Java
plugins {
  id 'java'
  id 'org.springframework.boot' version '3.2.2'
  id 'io.spring.dependency-management' version '1.1.4'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

java {
  sourceCompatibility = '21'
}

repositories {
  mavenCentral()
}

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
  implementation 'org.springframework.boot:spring-boot-starter-security'
  implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
  implementation 'org.springframework.boot:spring-boot-starter-web'
  implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
  runtimeOnly 'org.postgresql:postgresql'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
  testImplementation 'org.springframework.security:spring-security-test'
}

tasks.named('test') {
  useJUnitPlatform()
}
この部分

Java
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
がSpring Securityで追加された部分なんですが末尾にバージョンを示す「6」と書かれています。これは6系(6.x)という意味なんですが、このSpring Securityは「5.4」から書き方が大幅に変わり、以前の書き方は非推奨となっています。

もし、SLスタジオ以外でSpring Securityを学ぶ場合は昔の記事が多いので十分に注意して下さい。

データベースもいつものように作成しましょう。DB名は「slstudiomini」で良いです。

application.properties

PlainText
# データベース接続情報
spring.datasource.url=jdbc:postgresql://localhost:5432/slstudiomini
spring.datasource.username=springboot_user
spring.datasource.password=springboot_password

# Hibernate設定
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Spring Bootの日付形式を変更する
spring.mvc.format.date=yyyy-MM-dd
Spring Securityの最低限の設定
Spring Securityはアプリケーションの仕様によって柔軟にカスタマイズできるようになっていますが、ある程度Spring Securityの仕様を把握しないと難しいので今回は最小限の設定を行いましょう。



Userエンティティの作成

ログイン機能という事は最低でもログインするユーザーがいるという事です。SpringSecurityを利用する場合最低でも

ID

ユニークなアカウント

ハッシュ化(暗号化)されたパスワード

ユーザーアカウントが有効か無効かを示すフラグ

この４つが必要です。

更に今回はロールの制御も行うので、ロールを管理するテーブルと多対多の関係を作っておきます。

それでは早速作成してみましょう。

model/User.java

Java
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
@Table(name = "users")
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authority",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    public User() {
    }

    public User(Long id, String username, String password, boolean enabled, Set<Authority> authorities) {
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