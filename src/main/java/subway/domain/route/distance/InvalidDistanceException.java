package subway.domain.route.distance;

import subway.exception.BusinessException;

public class InvalidDistanceException extends BusinessException {

    public InvalidDistanceException() {
        super("거리는 0 이상의 정수만 입력 가능합니다.");
    }
}
