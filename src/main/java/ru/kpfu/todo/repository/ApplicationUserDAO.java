package ru.kpfu.todo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kpfu.todo.entity.ApplicationUser;
import ru.kpfu.todo.entity.GlobalPermission;
import ru.kpfu.todo.entity.Todo;
import ru.kpfu.todo.enumiration.GlobalPermissionName;
import ru.kpfu.todo.enumiration.Priority;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ApplicationUserDAO {
    private final JdbcTemplate jdbcTemplate;

    public Optional<ApplicationUser> findByEmail(String email) {
        String sql = "SELECT * FROM application_user WHERE email = ?";
        ApplicationUser user = jdbcTemplate.queryForObject(sql, new ApplicationUserMapper(), email);
        if (user==null) {
            return Optional.empty();
        }
        user.setTodoList(findTodosByUserId(user.getId()));
        user.setGlobalPermissions(findPermissionsByUserId(user.getId()));
        return Optional.of(user);
    }

    public void save(ApplicationUser user) {
        String sql = "INSERT INTO application_user (username, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword());
    }

    public List<ApplicationUser> findAll() {
        String sql = "SELECT * FROM application_user";
        List<ApplicationUser> users = jdbcTemplate.query(sql, new ApplicationUserMapper());
        for (ApplicationUser user : users) {
            user.setTodoList(findTodosByUserId(user.getId()));
            user.setGlobalPermissions(findPermissionsByUserId(user.getId()));
        }
        return users;
    }

    public Optional<ApplicationUser> findById(Long id) {
        String sql = "SELECT * FROM application_user WHERE id = ?";
        ApplicationUser user = jdbcTemplate.queryForObject(sql, new ApplicationUserMapper(), id);
        if (user == null) {
            return Optional.empty();
        }
        user.setTodoList(findTodosByUserId(user.getId()));
        user.setGlobalPermissions(findPermissionsByUserId(user.getId()));
        return Optional.of(user);
    }

    public void deleteById(Long id) {
        String deleteUserTodosSql = "DELETE FROM user_todo WHERE user_id = ?";
        jdbcTemplate.update(deleteUserTodosSql, id);

        String deleteUserSql = "DELETE FROM application_user WHERE id = ?";
        jdbcTemplate.update(deleteUserSql, id);

        String deleteGlobalPermissionSql = "DELETE FROM user_permission WHERE user_id = ?";
        jdbcTemplate.update(deleteGlobalPermissionSql,id);
    }






    private static class ApplicationUserMapper implements RowMapper<ApplicationUser> {
        @Override
        public ApplicationUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return ApplicationUser.builder()
                    .id(rs.getLong("id"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .password(rs.getString("password"))
                    .build();
        }
    }

    private List<Todo> findTodosByUserId(Long userId) {
        String sql = """
                SELECT t.*
                FROM todo t
                JOIN user_todo ut ON t.id = ut.todo_id
                WHERE ut.user_id = ?
                """;
        return jdbcTemplate.query(sql, new Object[]{userId}, new TodoMapper());
    }
    private List<GlobalPermission> findPermissionsByUserId(Long userId) {
        String sql = """
                SELECT gp.*
                FROM global_permission gp
                JOIN user_permission up ON gp.id = up.permission_id
                WHERE up.user_id = ?
                """;
        return jdbcTemplate.query(sql, new Object[]{userId}, new GlobalPermissionMapper());
    }
    private static class TodoMapper implements RowMapper<Todo> {
        @Override
        public Todo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Todo.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .createdDate(rs.getTimestamp("created_date").toLocalDateTime())
                    .dueDate(rs.getTimestamp("due_date") != null ? rs.getTimestamp("due_date").toLocalDateTime() : null)
                    .isCompleted(rs.getBoolean("is_completed"))
                    .name(Priority.valueOf(rs.getString("priority")))
                    .build();
        }
    }

    private static class GlobalPermissionMapper implements RowMapper<GlobalPermission> {
        @Override
        public GlobalPermission mapRow(ResultSet rs, int rowNum) throws SQLException {
            return GlobalPermission.builder()
                    .id(rs.getLong("id"))
                    .name(GlobalPermissionName.valueOf(rs.getString("name")))
                    .build();
        }
    }
}
