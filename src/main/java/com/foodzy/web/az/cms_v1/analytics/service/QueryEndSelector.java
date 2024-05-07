package com.foodzy.web.az.cms_v1.analytics.service;

import lombok.Getter;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QueryEndSelector {

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

    public QueryEndSelector() {
        this(null, null, false);
    }

    public QueryEndSelector(boolean unprocessed) {
        this(null, null, unprocessed);
    }

    public QueryEndSelector(ZoneOffset zoneOffset) {
        this(zoneOffset, null, false);
    }

    public QueryEndSelector(ZoneOffset zoneOffset, boolean unprocessed) {
        this(zoneOffset, null, unprocessed);
    }

    public QueryEndSelector(ZoneOffset zoneOffset, ZoneId zoneId, boolean unprocessed) {
//        this.zoneOffset = null == zoneOffset ? ZoneOffset.UTC : zoneOffset;
        this.zoneId = Objects.nonNull(zoneId) ? zoneId : (Objects.nonNull(zoneOffset) ? zoneOffset : ZoneOffset.UTC);
//        this.defaultInstant = unprocessed ?
//                Instant.now().truncatedTo(ChronoUnit.HOURS).plus(Duration.ofHours(1)) :
//                Instant.now().truncatedTo(ChronoUnit.HOURS).minus(Duration.ofHours(2));
        this.defaultInstant = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(Duration.ofHours(1));
        this.instantCandidates = new ArrayList<>();
        this.instantCandidates.add(this.defaultInstant);
    }

    public QueryEndSelector add(Instant instant) {
        if (Objects.nonNull(instant))
            this.instantCandidates.add(instant.truncatedTo(ChronoUnit.HOURS).plusSeconds(3600));
        return this;
    }

    public QueryEndSelector addUserQuery(LocalDate localDate) {
        if (Objects.nonNull(localDate))
            this.instantCandidates.add(localDate.atStartOfDay(this.zoneId).plusDays(1).toInstant());
        return this;
    }

    public QueryEndSelector addUserQueryNoPlus(LocalDate localDate) {
        if (Objects.nonNull(localDate))
            this.instantCandidates.add(localDate.atStartOfDay(this.zoneId).toInstant());
        return this;
    }

    /**
     * Select the {@link Instant} from the list of candidates. This simply sorts and selects the earliest
     * {@link Instant}.
     *
     * @return
     */
    public Instant select() {
        this.instantCandidates = this.instantCandidates
                .stream()
                .filter(Objects::nonNull)
                .sorted(Instant::compareTo)
                .collect(Collectors.toList());
        return this.instantCandidates.get(0);
    }
}

