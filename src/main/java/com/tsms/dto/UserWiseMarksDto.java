package com.tsms.dto;

import java.util.List;

public class UserWiseMarksDto {
    private Long id;

    private UserDto user;

    private List<MarksDto> marks ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<MarksDto> getMarks() {
        return marks;
    }

    public void setMarks(List<MarksDto> marks) {
        this.marks = marks;
    }

    public UserWiseMarksDto() {
    }

    public UserWiseMarksDto(Long id, UserDto user, List<MarksDto> marks) {
        this.id = id;
        this.user = user;
        this.marks = marks;
    }
}
