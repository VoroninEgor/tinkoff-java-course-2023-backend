package edu.java.util.mapper;

import edu.java.dto.link.LinkUpdateResponse;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkForUpdateResponseMapper implements RowMapper<LinkUpdateResponse> {
    @Override
    public LinkUpdateResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        URI url = URI.create(rs.getString("url"));
        Timestamp lastCheck = rs.getTimestamp("last_check");
        OffsetDateTime offsetDateTimeUpdatedAt = OffsetDateTime.of(lastCheck.toLocalDateTime(), ZoneOffset.UTC);

        return new LinkUpdateResponse(id, url, offsetDateTimeUpdatedAt);
    }
}
