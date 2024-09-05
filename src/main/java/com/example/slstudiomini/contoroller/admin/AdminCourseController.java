package com.example.slstudiomini.controller.admin;

import java.util.List;  // 必要なクラスをインポートします。

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.slstudiomini.model.Course;  // Course エンティティをインポートします。
import com.example.slstudiomini.service.CourseService;  // CourseService サービスをインポートします。

@Controller  // このクラスがコントローラであることを示します。
@RequestMapping("/admin/courses")  // "/admin/courses" で始まるリクエストに対応することを示します。
public class AdminCourseController {

    @Autowired  // Springにより、courseServiceを自動的にインジェクションします。
    private CourseService courseService;


    @GetMapping
    public String index(Model model) {
        List<Course> courses = courseService.findAllCourses();
        model.addAttribute("courses", courses);
        return "admin/course-list.html";
    }

    // コース追加のためのフォームを表示します。
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("course", new Course());  // 新しい Course オブジェクトをモデルに追加します。
        // Cource()はコンストラクタ呼び出し
        return "admin/course-add.html";  // "admin/course-add.html" テンプレートを表示します。
    }

    // コースをデータベースに追加します。
    @PostMapping("/add")
    public String add(Course course) {
        courseService.save(course);  // CourseService を使用して、コースを保存します。
        return "redirect:/admin/courses";  // コース一覧ページにリダイレクトします。
    }

    // コース編集のためのフォームを表示します。
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);  // 指定されたIDに基づいてコースを検索します。
        model.addAttribute("course", course);  // コースをモデルに追加します。
        return "/admin/course-edit";  // コース一覧ページにリダイレクトします。
    }

    @PostMapping("/edit")
    public String edit(Course course) {
        courseService.update(course);
        return "redirect:/admin/courses";
    }

    // コース削除の確認フォームを表示します。
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findCourseById(id);  // 指定されたIDに基づいてコースを検索します。
        model.addAttribute("course", course);  // コースをモデルに追加します。
        return "/admin/course-delete";  // "admin/course-delete" テンプレートを表示します。
    }

    // コースをデータベースから削除します。
    @PostMapping("/delete")
    public String delete(Course course) {
        courseService.delete(course);  // CourseService を使用して、コースを削除します。
        return "redirect:/admin/courses";  // コース一覧ページにリダイレクトします。
    }
}
