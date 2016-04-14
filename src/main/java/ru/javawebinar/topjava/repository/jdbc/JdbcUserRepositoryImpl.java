package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<Roles> ROLES_ROW_MAPPER = (rs, rowNum) -> {
        Roles roles = new Roles();
        roles.setUser_id(rs.getInt("user_id"));
        roles.setRole(rs.getString("role"));
        return roles;
    };

    private static class Roles {
        Integer user_id;

        String role;

        Integer getUser_id() {
            return user_id;
        }

        void setUser_id(Integer user_id) {
            this.user_id = user_id;
        }

        String getRole() {
            return role;
        }

        void setRole(String role) {
            this.role = role;
        }
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());
            saveWithRoles(user.getId(), user.getRoles());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map);
            updateWithRoles(user.getId(), user.getRoles());
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        deleteWithRoles(id);
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return getWithRoles(id);
    }

    @Override
    public User getByEmail(String email) {
        User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return getWithRoles(user.getId());
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);
        return users.stream().map(u -> getWithRoles(u.getId())).sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).sorted((o1, o2) -> o1.getEmail().compareTo(o2.getEmail())).collect(Collectors.toList());
    }

    private User getWithRoles(int id) {
        User user = DataAccessUtils.singleResult(jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id));
        Set<Role> roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?", ROLES_ROW_MAPPER, id).stream().map(role -> Role.valueOf(role.getRole())).collect(Collectors.toSet());
        if (!roles.isEmpty()) {
            user.setRoles(roles);
        } else {
            throw new NotFoundException(String.format(Locale.ENGLISH, "Not found roles for user: %d", id));
        }
        return user;
    }

    private void saveWithRoles(int userId, Set<Role> roles) {
        jdbcTemplate.execute("INSERT INTO user_roles (user_id, role) VALUES (" + userId + ", '" + getRolesAsString(roles) + "')");
    }

    private void updateWithRoles(int userId, Set<Role> roles) {
        jdbcTemplate.execute("UPDATE user_roles SET role='" + getRolesAsString(roles) + "' WHERE user_id=" + userId);
    }

    private void deleteWithRoles(int userId) {
        jdbcTemplate.execute("DELETE FROM user_roles WHERE user_id=" + userId);
    }

    private String getRolesAsString(Set<Role> roles) {
        for (Role role : roles) {
            if ((role.name() != null) && !(role.name().isEmpty())) {
                return role.name();
            }
        }
        return null;
    }
}
