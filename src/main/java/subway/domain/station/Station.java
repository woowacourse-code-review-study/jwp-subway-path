package subway.domain.station;

import java.util.Objects;

public class Station {

        private final Long id;
        private final Name name;

        private Station(final Long id, final String name) {
            this.id = id;
            this.name = new Name(name);
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name.getValue();
        }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
