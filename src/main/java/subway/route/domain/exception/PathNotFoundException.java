package subway.route.domain.exception;

import subway.common.exception.BusinessException;

public class PathNotFoundException extends BusinessException {

    public PathNotFoundException() {
        super("존재하지 않는 경로입니다.");
    }
}
