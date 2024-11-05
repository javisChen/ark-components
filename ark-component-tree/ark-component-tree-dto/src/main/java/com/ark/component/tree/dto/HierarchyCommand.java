package com.ark.component.tree.dto;

import lombok.Data;

@Data
public class HierarchyCommand {

    private String bizType;
    private Long id;
    private Long parentId;
    private Integer sequence;

    public HierarchyCommand(Long id, Long parentId, Integer sequence) {
        this.id = id;
        this.parentId = parentId;
        this.sequence = sequence;
    }


    public HierarchyCommand(Long id, Long parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public HierarchyCommand(String bizType, Long id, Long parentId, Integer sequence) {
        this.bizType = bizType;
        this.id = id;
        this.parentId = parentId;
        this.sequence = sequence;
    }

    public HierarchyCommand(String bizType, Long id, Long parentId) {
        this.parentId = parentId;
        this.id = id;
        this.bizType = bizType;
        this.sequence = 0;
    }

}
