package edu.java.util.mapper;

import edu.java.dto.tgchatlinks.TgChatResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TgChatResponseMapper implements RowMapper<TgChatResponse> {
    @Override
    public TgChatResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        Timestamp createdAt = rs.getTimestamp("created_at");
        OffsetDateTime offsetDateTime = OffsetDateTime.of(createdAt.toLocalDateTime(), ZoneOffset.UTC);
        return TgChatResponse.builder()
            .id(id)
            .createdAt(offsetDateTime)
            .build();
    }
}
