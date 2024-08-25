import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.slstudiomini.model.User;

// UserDetailsを継承しているので
// Spring Securityの認証クラスとして認識される
public class CustomUserDetails implements UserDetails {
    // 自作のUserクラスを持つように拡張する
    private User user;  // 自作のUserエンティティを保持するフィールド

    // CustomUserDetailsのコンストラクタ
    public CustomUserDetails(User user) {
        this.user = user;  // 渡されたUserエンティティをフィールドに設定
    }

    // ユーザーに付与されている権限を返すメソッド
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream()  // Userエンティティの権限をストリームに変換
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthority())) 
            // 各権限をSpring SecurityのSimpleGrantedAuthorityに変換
            .collect(Collectors.toList());  // 変換後の権限リストを返す
    }

    // ユーザーのパスワードを返すメソッド
    @Override
    public String getPassword() {
        return user.getPassword(); // カスタム User エンティティのパスワードを返す
    }

    // ユーザー名を返すメソッド
    @Override
    public String getUsername() {
        return user.getUsername(); // カスタム User エンティティのユーザー名を返す
    }

    // アカウントが期限切れでないかを示すメソッド
    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントが期限切れでないかを示す（trueなら期限切れでない）
    }

    // アカウントがロックされていないかを示すメソッド
    @Override
    public boolean isAccountNonLocked() {
        return true; // アカウントがロックされていないかを示す（trueならロックされていない）
    }

    // 資格情報（パスワード）が期限切れでないかを示すメソッド
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 資格情報が期限切れでないかを示す（trueなら期限切れでない）
    }

    // ユーザーが有効かどうかを示すメソッド
    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // カスタム User エンティティでユーザーが有効かどうかを返す
    }

    // カスタム User エンティティにアクセスするための追加メソッド
    public User getUser() {
        return user;  // 保持しているUserエンティティを返す
    }
}
