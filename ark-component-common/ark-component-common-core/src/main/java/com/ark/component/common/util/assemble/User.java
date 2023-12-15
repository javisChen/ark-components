package com.ark.component.common.util.assemble;

import com.ark.component.common.util.assemble.v2.FieldsAssemblerV2;
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

        FieldsAssemblerV2.execute(FieldAssembleConfig.<User, Role>builder()
                .condition(true)
                .records(List.of(new User()))
                .recordId(User::getId)
                .datasource(RoleQuery::byUserIds)
                .field(User::setRoleList)
                .bindingKey(Role::getUserId)
                .build());

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
