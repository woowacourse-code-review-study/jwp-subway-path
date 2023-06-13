package subway.line.application;

import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.line.Line;
import subway.line.domain.line.LineRepository;
import subway.line.domain.line.exception.LineNotFoundException;
import subway.line.dto.request.LineAddStationRequest;
import subway.line.dto.request.LineCreateRequest;
import subway.line.dto.request.LineUpdateInfoRequest;
import subway.line.dto.response.LineResponse;
import subway.station.domain.StationDeletedEvent;

@Service
@Transactional
public class LineCommandService {

    private final LineRepository lineRepository;

    public LineCommandService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse createLine(LineCreateRequest lineCreateRequestDto) {
        Line line = new Line(lineCreateRequestDto.getName(), lineCreateRequestDto.getColor(),
                lineCreateRequestDto.getUpStationId(), lineCreateRequestDto.getDownStationId(),
                lineCreateRequestDto.getDistance());
        Line savedLine = lineRepository.save(line);
        return LineResponse.from(savedLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void addInterStation(long id, LineAddStationRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.addInterStation(request.getUpStationId(), request.getDownStationId(), request.getNewStationId(),
                request.getDistance());
        lineRepository.update(line);
    }

    @EventListener(StationDeletedEvent.class)
    public void removeStation(StationDeletedEvent event) {
        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            line.deleteStation(event.getId());
            lineRepository.update(line);
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
