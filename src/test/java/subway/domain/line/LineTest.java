package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidSectionException;

class LineTest {

    @Nested
    @DisplayName("노선 역 추가 시 ")
    class AddSection {

        private Line line;
        private Station upward;
        private Station downward;

        @BeforeEach
        void setUp() {
            upward = new Station(1L, "잠실역");
            downward = new Station(2L, "종합운동장역");
            final List<Section> sections = List.of(
                    new Section(upward, downward, 10),
                    new Section(downward, Station.TERMINAL, 0)
            );
            line = new Line(1L, "2호선", "초록색", new ArrayList<>(sections));
        }

        @Test
        @DisplayName("노선에 역을 최초 추가한다.")
        void addFirstSection() {
            //given
            final Station upward = new Station(1L, "잠실역");
            final Station downward = new Station(2L, "종합운동장역");

            final Line line = new Line(1L, "2호선", "초록색");

            //when
            line.addSection(upward, downward, 10);

            //then
            final List<Station> result = line.show();
            assertThat(result).containsExactly(upward, downward);
        }

        @Test
        @DisplayName("중간에 상행역을 추가한다.")
        void addUpwardSection() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(additionStation, downward, 5);

            //then
            final List<Station> result = line.show();
            assertThat(result).containsExactly(upward, additionStation, downward);
        }

        @Test
        @DisplayName("중간에 하행역을 추가한다.")
        void addDownwardSection() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(upward, additionStation, 5);

            //then
            final List<Station> result = line.show();
            assertThat(result).containsExactly(upward, additionStation, downward);
        }

        @Test
        @DisplayName("맨 앞에 역을 추가한다.")
        void addSectionAtFirst() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(additionStation, upward, 5);

            //then
            final List<Station> result = line.show();
            assertThat(result).containsExactly(additionStation, upward, downward);
        }

        @Test
        @DisplayName("맨 뒤에 역을 추가한다.")
        void addSectionAtLast() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(downward, additionStation, 5);

            //then
            final List<Station> result = line.show();
            assertThat(result).containsExactly(upward, downward, additionStation);
        }

        @Test
        @DisplayName("역이 둘다 존재한다면 예외를 던진다.")
        void addSectionWithExistStations() {
            //given
            //when
            //then
            assertThatThrownBy(() -> line.addSection(upward, downward, 5))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("두 역이 이미 노선에 존재합니다.");
        }

        @Test
        @DisplayName("역이 둘다 존재하지 않으면 예외를 던진다.")
        void addSectionWithoutExistStations() {
            //given
            final Station newUpward = new Station(3L, "잠실새내역");
            final Station newDownward = new Station(4L, "사당역");

            //when
            //then
            assertThatThrownBy(() -> line.addSection(newUpward, newDownward, 5))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("연결할 역 정보가 없습니다.");
        }

        @Test
        @DisplayName("역이 둘다 존재하지 않으면 예외를 던진다.")
        void addSectionWithInvalidRangeDistance() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            //then
            assertThatThrownBy(() -> line.addSection(upward, additionStation, 10))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessage("추가될 역의 거리는 추가될 위치의 두 역사이의 거리보다 작아야합니다.");
        }
    }
}
