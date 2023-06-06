package subway.line.ui.dto.response;

import java.util.List;
import subway.line.application.dto.response.LineResponseDto;

public class LinesResponseDto {

    private List<LineResponseDto> lines;

    private LinesResponseDto() {
    }

    public LinesResponseDto(List<LineResponseDto> lines) {
        this.lines = lines;
    }

    public List<LineResponseDto> getLines() {
        return lines;
    }
}
