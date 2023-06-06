package subway.line.application;

import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.response.LineResponseDto;
import subway.line.application.exception.LineNotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.ui.dto.request.LineAddStationRequest;
import subway.line.ui.dto.request.LineCreateRequest;
import subway.line.ui.dto.request.LineUpdateInfoRequest;
import subway.station.domain.StationDeletedEvent;

@Service
@Transactional
public class LineCommandService {

    private final LineRepository lineRepository;

    public LineCommandService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponseDto createLine(LineCreateRequest lineCreateRequestDto) {
        Line line = new Line(lineCreateRequestDto.getName(), lineCreateRequestDto.getColor(),
                lineCreateRequestDto.getUpStationId(), lineCreateRequestDto.getDownStationId(),
                lineCreateRequestDto.getDistance());
        Line savedLine = lineRepository.save(line);
        return LineResponseDto.from(savedLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponseDto addInterStation(long id, LineAddStationRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.addInterStation(request.getUpStationId(), request.getDownStationId(), request.getNewStationId(),
                request.getDistance());
        Line result = lineRepository.update(line);
        return LineResponseDto.from(result);
    }

    @EventListener(StationDeletedEvent.class)
    public void removeStation(StationDeletedEvent event) {
        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            line.deleteStation(event.getId());
            Line result = lineRepository.update(line);
            removeIfNoStationInLine(result);
        }
    }

    private void removeIfNoStationInLine(Line result) {
        if (result.isEmpty()) {
            lineRepository.deleteById(result.getId());
        }
    }

    public void updateLine(long id, LineUpdateInfoRequest request) {
        Line line = lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
        line.updateColor(request.getColor());
        line.updateName(request.getName());
        lineRepository.update(line);
    }
}
