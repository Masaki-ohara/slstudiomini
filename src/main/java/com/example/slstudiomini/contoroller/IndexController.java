package com.example.slstudiomini.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// @Controller アノテーションを使用して、このクラスがSpring MVCのコントローラーであることを示します。
// コメントアウトされていたため、コード修正後にはアノテーションを正しく記述しました。
@Controller
public class IndexController {

    // @GetMapping アノテーションは、このメソッドがHTTP GETリクエストを処理することを指定します。
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        // @AuthenticationPrincipal アノテーションを使って、現在認証されているユーザーの情報を取得します。
        // UserDetails オブジェクトとして currentUser を取得します。

        // model.addAtribute("username", currentUser.getUsername());
        // ↑はタイプミスです。addAtribute は addAttribute が正しいメソッド名です。

        // 現在のユーザー名を "username" というキーでモデルに追加します。
        model.addAttribute("username", currentUser.getUsername());

        // "index" というビュー（通常は Thymeleaf テンプレート）を返します。
        // このビューは src/main/resources/templates/index.html に対応します。
        return "index";
    }
}
