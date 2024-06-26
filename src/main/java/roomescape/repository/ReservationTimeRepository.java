package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dto.ReservationTimeRq;
import roomescape.model.ReservationTime;

import java.sql.*;
import java.util.List;

@Repository
public class ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, new ReservationTimeRowMapper());
    }

    private static class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
        @Override
        public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReservationTime reservationTime = new ReservationTime();
            reservationTime.setId(rs.getLong("id"));
            reservationTime.setStartAt(rs.getString("start_at"));
            return reservationTime;
        }
    }

    public ReservationTime findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new ReservationTimeRowMapper());
    }

    public Long save(ReservationTimeRq reservationTimeRq) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, reservationTimeRq.getStartAt());
                return ps;
            }
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
