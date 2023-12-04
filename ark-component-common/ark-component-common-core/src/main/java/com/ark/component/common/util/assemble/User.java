package com.ark.component.common.util.assemble;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class User {

    private Long id;
    private List<Role> roleList;
    private List<Group> groupList;

    public static void main(String[] args) {
        User user = new User();
        user.setId(1L);
        FieldsAssembler.execute(Lists.newArrayList(user),
                User::getId,
                RoleQuery::byUserIds,
                User::setRoleList,
                Role::getUserId);
        ;
    }

    @Data
    private static class RoleQuery {

        public static List<Role> byUserIds(List<Long> userIds) {
            return Lists.newArrayList();
        }

    }

    @Data
    private static class Role {
        private Long id;
        private Long userId;
    }

    @Data
    private static class Group {
        private Long id;
        private Long userId;
    }
}
