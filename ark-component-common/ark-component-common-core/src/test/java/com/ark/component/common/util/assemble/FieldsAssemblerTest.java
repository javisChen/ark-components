package com.ark.component.common.util.assemble;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FieldsAssemblerTest {

    @org.junit.jupiter.api.Test
    void fill() {
        List<User> dataset = List.of(new User());
        FieldsAssembler.fill(FieldAssembleConfig.<User, User.Role, Long>Multiple()
                .condition(true)
                .dataSet(dataset)
                .queryKey(User::getRoleId)
                .resultSet(User.RoleQuery::byIds)
                .collectionField(User::setRoleList)
                .mapKey(User.Role::getId)
                .build());
        System.out.println(dataset);
    }

    @org.junit.jupiter.api.Test
    void fillSingle() {
        List<User> dataset = List.of(new User());
        FieldsAssembler.fillSingle(FieldAssembleConfig.<User, User.Type, Long>Single()
                .condition(true)
                .dataSet(dataset)
                .queryKey(User::getId)
                .resultSet(User.TypeQuery::byIds)
                .field((user1, type) -> user1.setType(type.getName()))
                .mapKey(User.Type::getId)
                .build());
    }

    @org.junit.jupiter.api.Test
    void fillNew() {
        ArrayList<User> users = new ArrayList<>();
        User user = new User();
        user.setRoleId(1L);
        user.setTypeId(1L);
        users.add(user);

        DataProcessor<User> processor = new DataProcessor<>(users);

        processor
                .keySelect(User::getRoleId)
                .query(User.RoleQuery::byIds)
                .keyBy(User.Role::getId)
                .collection()
                .process(User::setRoleList);

        processor
                .keySelect(User::getTypeId)
                .query(User.TypeQuery::byIds)
                .keyBy(User.Type::getId)
                .object()
                .process((u, type) -> u.setType(type.getName()));

        System.out.println(users);
    }

    @Data
    public static class User {

        private Long id;
        private String name;
        private List<Role> roleList;
        private List<Group> groupList;
        private String type;
        private Long typeId;
        private Long roleId;

        @Data
        private static class RoleQuery {

            private final static Map<Long, Role> map = new HashMap<>();

            static {
                map.put(1L, new Role(1L, "CTO"));
                map.put(2L, new Role(2L, "CEO"));
            }

            public static List<Role> byIds(List<Long> ids) {
                List<Role> roles = new ArrayList<>();
                for (Long id : ids) {
                    Role role = map.get(id);
                    if (role != null) {
                        roles.add(role);
                    }
                }
                return roles;
            }

        }

        @Data
        private static class TypeQuery {


            private final static Map<Long, Type> map = new HashMap<>();

            static {
                map.put(1L, new Type(1L, "内部用户"));
                map.put(2L, new Type(2L, "外部用户"));
            }

            public static List<Type> byIds(List<Long> ids) {
                List<Type> types = new ArrayList<>();
                for (Long id : ids) {
                    Type type = map.get(id);
                    if (type != null) {
                        types.add(type);
                    }
                }
                return types;
            }

        }

        @Data
        @AllArgsConstructor
        private static class Role {
            private Long id;
            private String roleName;
        }

        @Data
        @AllArgsConstructor
        private static class Type {
            private Long id;
            private String name;
        }

        @Data
        private static class Group {
            private Long id;
            private Long userId;
        }
    }

}