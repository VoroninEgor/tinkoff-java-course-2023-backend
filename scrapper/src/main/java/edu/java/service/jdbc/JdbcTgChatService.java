package edu.java.service.jdbc;

import edu.java.dao.jdbc.JdbcTgChatDao;
import edu.java.dto.tgchatlinks.TgChatResponse;
import edu.java.exception.TgChatAlreadyRegisteredException;
import edu.java.service.TgChatService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JdbcTgChatService implements TgChatService {

    private final JdbcTgChatDao tgChatRepository;

    @Transactional
    @Override
    public void unregister(Long id) {
        tgChatRepository.remove(id);
    }

    @Transactional
    @Override
    public void register(Long id) {
        try {
            tgChatRepository.save(id);
        } catch (DuplicateKeyException e) {
            throw new TgChatAlreadyRegisteredException();
        }
    }

    @Override
    public List<Long> fetchTgChatsIdByLink(URI link) {
        return tgChatRepository.fetchTgChatsIdByLink(link.toString());
    }

    @Override
    public TgChatResponse fetchById(Long id) {
        return tgChatRepository.fetchById(id);
    }
}
