package com.ark.component.tree;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TreeTest extends ApplicationTests {


    @Autowired
    private TreeService treeService;

    @Test
    public void tests() {
        treeService.addNode("Menu", 1L);
    }

}
