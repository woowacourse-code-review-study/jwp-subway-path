package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.domain.fixture.SectionFixtures.포함된_구간들을_검증한다;
import static subway.domain.fixture.StationFixture.경유역1;
import static subway.domain.fixture.StationFixture.경유역2;
import static subway.domain.fixture.StationFixture.선릉;
import static subway.domain.fixture.StationFixture.없는역;
import static subway.domain.fixture.StationFixture.없는역2;
import static subway.domain.fixture.StationFixture.잠실;
import static subway.domain.fixture.StationFixture.잠실나루;
import static subway.domain.fixture.StationFixture.종착역;
import static subway.domain.fixture.StationFixture.출발역;
import static subway.exception.line.LineExceptionType.ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN;
import static subway.exception.line.LineExceptionType.ALREADY_EXIST_STATIONS;
import static subway.exception.line.LineExceptionType.DELETED_STATION_NOT_EXIST;
import static subway.exception.line.LineExceptionType.NO_RELATION_WITH_ADDED_SECTION;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.BaseExceptionType;
import subway.exception.line.LineException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Line 은(는)")
class LineTest {

    private void 포함된_노선들을_검증한다(final Line line, final String... sectionStrings) {
        포함된_구간들을_검증한다(line.sections(), sectionStrings);
    }

    @Nested
    class 노선에_구간_추가시 {

        @Test
        void 중간에_추가할_수_있다() {
            // given
            // 출발역 -[10km]- 종착역
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 종착역, 10)));
            // 출발역 -[6km]- 경유역2 -[4km]- 종착역
            final Section middle1 = new Section(경유역2, 종착역, 4);
            // 출발역 -[1km]- 경유역1 -[5km]- 경유역2 -[4km]- 종착역
            final Section middle2 = new Section(출발역, 경유역1, 1);

            // when
            line.addSection(middle1);
            line.addSection(middle2);

            // then
            포함된_노선들을_검증한다(line,
                    "출발역-[1km]-경유역1",
                    "경유역1-[5km]-경유역2",
                    "경유역2-[4km]-종착역"
            );
        }

        @Test
        void 상행_종점에_추가할_수_있다() {
            // given
            // 잠실 -[10km]- 종착역
            final Line line = new Line("1호선",
                    new Sections(new Section(잠실, 종착역, 10)));
            // 선릉 - [7km] - 잠실 -[10km]- 종착역
            final Section middle = new Section(선릉, 잠실, 7);
            // 출발역 - [1km] - 선릉 - [7km] - 잠실 -[10km]- 종착역
            final Section top = new Section(출발역, 선릉, 1);

            // when
            line.addSection(middle);
            line.addSection(top);

            // then
            포함된_노선들을_검증한다(line,
                    "출발역-[1km]-선릉",
                    "선릉-[7km]-잠실",
                    "잠실-[10km]-종착역"
            );
        }

        @Test
        void 하행_종점에_추가할_수_있다() {
            // given
            // 출발역 -[10km]- 잠실
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 잠실, 10)));
            // 출발역 -[10km]- 잠실 -[7km]- 선릉
            final Section middle = new Section(잠실, 선릉, 7);
            // 출발역 -[10km]- 잠실 -[7km]- 선릉 -[1km]- 종착역
            final Section down = new Section(선릉, 종착역, 1);

            // when
            line.addSection(middle);
            line.addSection(down);

            // then
            포함된_노선들을_검증한다(line,
                    "출발역-[10km]-잠실",
                    "잠실-[7km]-선릉",
                    "선릉-[1km]-종착역"
            );
        }

        @Test
        void 추가하려는_구간의_두_역이_이미_구간들에_포함되어있으면_예외() {
            // given
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 종착역, 10)));
            final Section middle1 = new Section(경유역2, 종착역, 4);
            final Section middle2 = new Section(출발역, 경유역1, 3);
            line.addSection(middle1);
            line.addSection(middle2);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    line.addSection(
                            new Section(출발역, 종착역, 1)
                    )).exceptionType();
            assertThat(exceptionType).isEqualTo(ALREADY_EXIST_STATIONS);
        }

        @Test
        void 추가하려는_구간의_두_역_모두_구간들에_존재하지_않으면_예외() {
            // given
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 종착역, 10)));
            final Section middle1 = new Section(경유역2, 종착역, 4);
            final Section middle2 = new Section(출발역, 경유역1, 1);
            line.addSection(middle1);
            line.addSection(middle2);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    line.addSection(
                            new Section(없는역, 없는역2, 15)
                    )).exceptionType();
            assertThat(exceptionType).isEqualTo(NO_RELATION_WITH_ADDED_SECTION);
        }

        @Test
        void 중간에_추가시_추가하려는_구간의_길이가_기존_구간의_길이와_같거나_크다면_예외() {
            // given
            // 출발역 -[10km]- 종착역
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 종착역, 10)));

            // 출발역 -[6km]- 경유역2 -[4km]- 종착역
            final Section middle1 = new Section(경유역2, 종착역, 4);
            line.addSection(middle1);

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    line.addSection(
                            new Section(경유역1, 경유역2, 6)
                    )).exceptionType();
            assertThat(exceptionType).isEqualTo(ADDED_SECTION_NOT_SMALLER_THAN_ORIGIN);
        }
    }

    @Nested
    class 노선에서_역_제거시 {

        @Test
        void 역을_제거하고_노선을_재조정한다() {
            // given
            final Line line = new Line("1호선", new Sections(List.of(
                    new Section(출발역, 잠실, 10),
                    new Section(잠실, 잠실나루, 5),
                    new Section(잠실나루, 종착역, 7)
            )));

            // when
            line.removeStation(잠실);

            // then
            포함된_노선들을_검증한다(line,
                    "출발역-[15km]-잠실나루",
                    "잠실나루-[7km]-종착역"
            );
        }

        @Test
        void 상행_종점_제거_가능() {
            // given
            final Line line = new Line("1호선", new Sections(List.of(
                    new Section(출발역, 잠실, 10),
                    new Section(잠실, 잠실나루, 5),
                    new Section(잠실나루, 종착역, 7)
            )));

            // when
            line.removeStation(출발역);

            // then
            포함된_노선들을_검증한다(line,
                    "잠실-[5km]-잠실나루",
                    "잠실나루-[7km]-종착역"
            );
        }

        @Test
        void 하행_종점_제거_가능() {
            // given
            final Line line = new Line("1호선", new Sections(List.of(
                    new Section(출발역, 잠실, 10),
                    new Section(잠실, 잠실나루, 5),
                    new Section(잠실나루, 종착역, 7)
            )));

            // when
            line.removeStation(종착역);

            // then
            포함된_노선들을_검증한다(line,
                    "출발역-[10km]-잠실",
                    "잠실-[5km]-잠실나루"
            );
        }

        @Test
        void 없는_역은_제거할_수_없다() {
            // given
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 종착역, 10)));

            // when & then
            final BaseExceptionType exceptionType = assertThrows(LineException.class, () ->
                    line.removeStation(없는역)
            ).exceptionType();
            assertThat(exceptionType).isEqualTo(DELETED_STATION_NOT_EXIST);
        }

        @Test
        void 노선에_역이_단_두개일_경우_하나의_역을_제거하면_나머지_역도_제거된다() {
            // given
            final Line line = new Line("1호선",
                    new Sections(new Section(출발역, 종착역, 10)));

            // when
            line.removeStation(출발역);

            // then
            assertThat(line.sections()).isEmpty();
        }
    }
}
