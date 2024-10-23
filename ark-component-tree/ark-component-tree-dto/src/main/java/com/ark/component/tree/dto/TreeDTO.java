package com.ark.component.tree.dto;

import lombok.Data;

@Data
public class TreeDTO<ID> {

    private ID id;
    private ID parentId;
    private String name;
    private String levelPath;
    private Integer level;
//
//    ID getBizId();
//
//    String getName();
//
//    void setLevelPath(String levelPath);
//
//    void setLevel(Integer level);
//
//    String getLevelPath();
//
//    Integer getLevel();
}
