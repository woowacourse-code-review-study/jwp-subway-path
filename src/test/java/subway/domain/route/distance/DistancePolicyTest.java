package subway.domain.route.distance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("거리 계산 정책은")
class DistancePolicyTest {

    @ParameterizedTest
    @DisplayName("거리에 따른 요금을 계산한다")
    @CsvSource(value = {"9,1250", "12,1350", "16,1450", "58,2150"})
    void calculateFare(final int input, final long expected) {
        // given
        final DistanceFareStrategy distancePolicy = DistancePolicy.from(input);

        // when
        final long result = distancePolicy.calculateFare(input);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
