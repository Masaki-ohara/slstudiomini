package com.example.slstudiomini.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.slstudiomini.model.Course;
import com.example.slstudiomini.repository.CourseRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service  // このクラスがSpringのサービスクラスであることを示します。
public class CourseService {

    @Autowired  // Springが自動的にCourseRepositoryのインスタンスを注入します。
    private CourseRepository courseRepository;

    // すべてのコースを取得するメソッド
    public List<Course> findAllCourses() {
        return courseRepository.findAll();  // CourseRepositoryを使用して、すべてのコースを取得します。
    }

    // 指定されたIDに基づいてコースを取得するメソッド
    public Course findCourseById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Course Not Found With id = " + id));
            // findByIdはOptionalを返すので、コースが見つからなかった場合はEntityNotFoundExceptionを投げます。
    }

    @Transactional  // このメソッドがトランザクションとして実行されることを示します。
    public Course save(Course course) {
        if (course != null) {  // courseがnullでない場合に処理を実行
            course.setCreatedAt(LocalDateTime.now());  // 現在の日時を作成日時に設定
            courseRepository.save(course);  // CourseRepositoryを使用して、コースを保存
        }
        return course;  // 保存されたコースを返します。
    }

    @Transactional  // このメソッドがトランザクションとして実行されることを示します。
    public Course update(Course updateCourse) {
        Course course = findCourseById(updateCourse.getId());  // 更新対象のコースを取得
        course.setName(updateCourse.getName());  // コース名を更新
        course.setDescription(updateCourse.getDescription());  // コースの説明を更新
        course.setUpdatedAt(LocalDateTime.now());  // 現在の日時を更新日時に設定

        return courseRepository.save(course);  // 更新されたコースを保存して返します。
    }

    @Transactional  // このメソッドがトランザクションとして実行されることを示します。
    public void delete(Course deletedCourse) {
        Course course = findCourseById(deletedCourse.getId());  // 削除対象のコースを取得
        course.setDeletedAt(LocalDateTime.now());  // 削除日時に現在の日時を設定
        course.getLessons().forEach(lesson -> lesson.setDeletedAt(LocalDateTime.now()));  
        // コースに関連するレッスンの削除日時も設定
        courseRepository.save(course);  // 削除日時が設定されたコースを保存
    }
}
