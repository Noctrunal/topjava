package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.repository.UserMealRepository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcUserMealRepositoryImpl implements UserMealRepository {
    private static final Logger LOG = getLogger(JdbcUserMealRepositoryImpl.class);

    private static final RowMapper<UserMeal> ROW_MAPPER = ((resultSet, rowNum) -> {
        UserMeal userMeal = new UserMeal();
        userMeal.setId(resultSet.getInt("id"));
        userMeal.setDescription(resultSet.getString("description"));
        userMeal.setCalories(resultSet.getInt("calories"));
        userMeal.setDateTime(resultSet.getTimestamp("datetime").toLocalDateTime());
        LOG.info("Row mapper {}", userMeal);
        return userMeal;
    });

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUserMeal;

    @Autowired
    public JdbcUserMealRepositoryImpl(DataSource dataSource) {
        this.insertUserMeal = new SimpleJdbcInsert(dataSource)
                .withTableName("MEALS")
                .usingGeneratedKeyColumns("id");
        LOG.info("init {} {}", dataSource.getClass().getSimpleName(), insertUserMeal.getClass().getSimpleName());
    }

    @Override
    public UserMeal save(UserMeal userMeal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("userId", userId);
        map.addValue("id", userMeal.getId());
        map.addValue("description", userMeal.getDescription());
        map.addValue("calories", userMeal.getCalories());
        map.addValue("dateTime", Timestamp.valueOf(userMeal.getDateTime()));

        if (userMeal.isNew()) {
            Number newKey = insertUserMeal.executeAndReturnKey(map);
            userMeal.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE meals SET description=:description, calories=:calories, datetime=:dateTime WHERE userid=:userId AND id=:id", map);
        }
        LOG.info("save {} {}", userMeal, userId);
        return userMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        LOG.info("delete {} {}", id, userId);
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND userid=?", id, userId) != 0;
    }

    @Override
    public UserMeal get(int id, int userId) {
        List<UserMeal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND userid=?", ROW_MAPPER, id, userId);
        LOG.info("get {}", meals);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<UserMeal> getAll(int userId) {
        List<UserMeal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE userid=? ORDER BY datetime DESC", ROW_MAPPER, userId);
        LOG.info("getAll {}", meals);
        return meals;
    }

    @Override
    public List<UserMeal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        List<UserMeal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE userid=? AND datetime BETWEEN ? AND ? ORDER BY datetime DESC",
                ROW_MAPPER, userId, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
        LOG.info("getBetween {}", meals);
        return meals;
    }
}
