package subway.infrastructure.shortestpath;

import java.util.List;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class LinesGraphAdapter extends WeightedMultigraph<Station, SectionAdapter> {

    private LinesGraphAdapter() {
        super(SectionAdapter.class);
    }

    public static LinesGraphAdapter adapt(final List<Line> lines) {
        final LinesGraphAdapter routeGraphAdapter = new LinesGraphAdapter();
        lines.stream()
                .flatMap(it -> it.sections().stream())
                .forEach(routeGraphAdapter::addSection);
        return routeGraphAdapter;
    }

    public void addSection(final Section section) {
        addVertex(section.up());
        addVertex(section.down());
        final SectionAdapter sectionAdapter1 = addEdge(section.up(), section.down());
        setEdgeWeight(sectionAdapter1, section.distance());
    }
}
