package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections = new ArrayList<>();

    public Sections(final Section section) {
        sections.add(section);
    }

    public Sections(final List<Section> sections) {
        validateSectionIsLinked(sections);
        this.sections.addAll(sections);
    }

    private void validateSectionIsLinked(final List<Section> sections) {
        for (int i = 0; i < sections.size() - 1; i++) {
            final Station down = sections.get(i).down();
            final Station nextUp = sections.get(i + 1).up();
            if (!down.equals(nextUp)) {
                throw new IllegalArgumentException("각 구간의 연결 상태가 올바르지 않습니다.");
            }
        }
    }

    public void addSection(final Section addedSection) {
        validateAlreadyExistStation(addedSection);
        if (isAddedToTerminal(addedSection)) {
            return;
        }
        addInMiddle(addedSection);
    }

    private void validateAlreadyExistStation(final Section addedSection) {
        final List<Station> stations = stations();
        if (stations.contains(addedSection.up())
                && stations.contains(addedSection.down())) {
            throw new IllegalArgumentException("추가하려는 두 역이 이미 포함되어 있습니다.");
        }
    }

    private boolean isAddedToTerminal(final Section addedSection) {
        final Section first = firstSection();
        final Section last = lastSection();
        if (first.isDownThan(addedSection)) {
            sections.add(0, addedSection);
            return true;
        }
        if (addedSection.isDownThan(last)) {
            sections.add(addedSection);
            return true;
        }
        return false;
    }

    private Section lastSection() {
        return sections.get(sections.size() - 1);
    }

    private Section firstSection() {
        return sections.get(0);
    }

    private void addInMiddle(final Section addedSection) {
        final int removedIdx = sections.indexOf(findRemovedSection(addedSection));
        final Section removedSection = sections.remove(removedIdx);
        final Section remain = removedSection.minus(addedSection);
        sections.add(removedIdx, judgeUpSection(remain, addedSection));
        sections.add(removedIdx + 1, judgeDownSection(remain, addedSection));
    }

    private Section findRemovedSection(final Section addedSection) {
        return sections.stream()
                .filter(addedSection::hasSameUpOrDownStation)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("두 구간이 연관관계가 없어 뺄 수 없습니다."));
    }

    private Section judgeUpSection(final Section remain, final Section addedSection) {
        if (remain.isDownThan(addedSection)) {
            return addedSection;
        }
        return remain;
    }

    private Section judgeDownSection(final Section remain, final Section addedSection) {
        if (remain.isDownThan(addedSection)) {
            return remain;
        }
        return addedSection;
    }

    public void removeStation(final Station removedStation) {
        validateStationIsExist(removedStation);
        if (sections.size() == 1) {
            sections.clear();
            return;
        }
        if (removedFromTerminal(removedStation)) {
            return;
        }
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).down().equals(removedStation)) {
                final Section up = sections.remove(i);
                final Section down = sections.remove(i);
                sections.add(i, up.plus(down));
                return;
            }
        }
    }

    private boolean removedFromTerminal(final Station removedStation) {
        final Station upTerminal = firstSection().up();
        final Station downTerminal = lastSection().down();
        if (upTerminal.equals(removedStation)) {
            sections.remove(firstSection());
            return true;
        }
        if (downTerminal.equals(removedStation)) {
            sections.remove(lastSection());
            return true;
        }
        return false;
    }

    private void validateStationIsExist(final Station removedStation) {
        final List<Station> stations = stations();
        if (!stations.contains(removedStation)) {
            throw new IllegalArgumentException("없는 역은 제거할 수 없습니다.");
        }
    }

    public List<Station> stations() {
        final List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).up());
        final List<Station> collect = sections.stream()
                .map(Section::down)
                .collect(Collectors.toList());
        stations.addAll(collect);
        return stations;
    }

    public List<Section> sections() {
        return sections;
    }
}
