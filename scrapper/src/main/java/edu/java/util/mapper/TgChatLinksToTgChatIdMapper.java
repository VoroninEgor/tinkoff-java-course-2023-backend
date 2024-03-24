package edu.java.util.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TgChatLinksToTgChatIdMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("tgchats_id");
    }
}
