package subway.dao;

import subway.entity.LineEntity;

import java.util.List;

public interface LineDao {

    LineEntity insert(LineEntity lineEntity);

    List<LineEntity> findAll();

    LineEntity findById(Long id);

    void update(LineEntity newLineEntity);

    void deleteById(Long id);
}
