package com.elearning.lessons.entity;

import com.elearning.courses.entity.Course;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String content;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "media_type")
    private String mediaType;

    @Column(name = "media_path")
    private String mediaPath;

    @Column(name = "media_duration")
    private Integer mediaDuration;

    @Column(name = "media_size")
    private Long mediaSize;

    @Column(name = "media_mime")
    private String mediaMime;

    @Column(name = "is_free_preview")
    private Boolean isFreePreview = false;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Lesson() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    public String getMediaPath() { return mediaPath; }
    public void setMediaPath(String mediaPath) { this.mediaPath = mediaPath; }
    public Integer getMediaDuration() { return mediaDuration; }
    public void setMediaDuration(Integer mediaDuration) { this.mediaDuration = mediaDuration; }
    public Long getMediaSize() { return mediaSize; }
    public void setMediaSize(Long mediaSize) { this.mediaSize = mediaSize; }
    public String getMediaMime() { return mediaMime; }
    public void setMediaMime(String mediaMime) { this.mediaMime = mediaMime; }
    public Boolean getIsFreePreview() { return isFreePreview; }
    public void setIsFreePreview(Boolean isFreePreview) { this.isFreePreview = isFreePreview; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
