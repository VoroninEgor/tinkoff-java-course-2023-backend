package edu.java.util.mapper;

import edu.java.dto.link.LinkResponse;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkResponseMapper implements RowMapper<LinkResponse> {
    @Override
    public LinkResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        URI url = URI.create(rs.getString("url"));

        return new LinkResponse(id, url);
    }
}
