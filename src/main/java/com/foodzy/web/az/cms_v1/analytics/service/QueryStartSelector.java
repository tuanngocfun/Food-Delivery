package com.foodzy.web.az.cms_v1.analytics.service;

import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryStartSelector {

    @Getter
    private final Instant defaultInstant;

//    @Getter
//    private final ZoneOffset zoneOffset;

    @Getter
    private final ZoneId zoneId;

    /**
     * List of {@link Instant} candidates to be selected.
     */
    @Getter
    private List<Instant> instantCandidates;

    public QueryStartSelector() {
        this(null);
    }

    public QueryStartSelector(ZoneOffset zoneOffset) {
        this(zoneOffset, null);
    }

    public QueryStartSelector(ZoneId zoneOffset, ZoneId zoneId) {
//        this.zoneOffset = null == zoneOffset ? ZoneOffset.UTC : zoneOffset;
        this.zoneId = Objects.nonNull(zoneId) ? zoneId : zoneOffset;
        this.defaultInstant = LocalDate
                .now()
                .atStartOfDay()
                .atZone(this.zoneId)
                .minusDays(30)
                .toInstant();
        this.instantCandidates = new ArrayList<>();
    }

    public QueryStartSelector add(Instant instant) {
        if (Objects.nonNull(instant))
            this.instantCandidates.add(instant.truncatedTo(ChronoUnit.HOURS));
        return this;
    }

    public QueryStartSelector addUserQuery(LocalDate localDate) {
        if (Objects.nonNull(localDate))
            this.instantCandidates.add(localDate.atStartOfDay(zoneId).toInstant());
        return this;
    }

    /**
     * Select the {@link Instant} from the list of candidates. This simply sorts and selects the latest {@link Instant}.
     *
     * @return
     */
    public Instant select() {
        this.instantCandidates = this.instantCandidates
                .stream()
                .filter(Objects::nonNull)
                .sorted((i1, i2) -> -i1.compareTo(i2))
                .collect(Collectors.toList());
        return this.instantCandidates.isEmpty() ? this.defaultInstant : this.instantCandidates.get(0);
    }
}

