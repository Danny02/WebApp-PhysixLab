package de.hofuniversity.iws.dto;

import java.io.Serializable;
import java.util.List;

public class SubjectAreaDTO implements Serializable {

    private boolean detached = false;
    
    private Long id;

    private String name;

    private List<LessonDTO> lessonList;

    public boolean isDetached() {
        return detached;
    }

    public void setDetached(boolean detached) {
        this.detached = detached;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LessonDTO> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<LessonDTO> lessonList) {
        this.lessonList = lessonList;
    }
}
