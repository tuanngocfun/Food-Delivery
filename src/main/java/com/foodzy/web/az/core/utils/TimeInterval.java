package com.foodzy.web.az.core.utils;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 * <p>
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <p>
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * * Neither the name of JSR-310 nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * <p>
 * A simplified version of
 * <a href="https://github.com/ThreeTen/threeten-extra/blob/master/src/main/java/org/threeten/extra/Interval.java">
 * ThreeTen - Interval class
 * </a>
 *
 * @version 1.0
 */
public final class TimeInterval implements Serializable {

    /**
     * The start instant (inclusive).
     */
    @Getter
    private final Instant start;

    /**
     * The end instant (exclusive).
     */
    @Getter
    private final Instant end;

    public static TimeInterval of(Instant startInclusive, Instant endExclusive) {
        Objects.requireNonNull(startInclusive, "startInclusive");
        Objects.requireNonNull(endExclusive, "endExclusive");
//        System.out.println("TimeInterval: " + startInclusive.toString() + " - " + endExclusive.toString());
//        if (endExclusive.isBefore(startInclusive))
//            throw new DateTimeException("End instant must be on or after start instant.");
        return new TimeInterval(startInclusive, endExclusive);
    }

    /**
     * Constructor.
     *
     * @param startInclusive The start instant, inclusive, validated not null
     * @param endExclusive   The end instant, exclusive, validated not null
     */
    private TimeInterval(Instant startInclusive, Instant endExclusive) {
        this.start = startInclusive;
        this.end = endExclusive;
    }

    /**
     * Checks if the range is empty.
     * <p>
     * An empty range occurs when the start date equals the inclusive end date.
     *
     * @return true if the range is empty
     */
    public boolean isEmpty() {
        return this.start.equals(this.end);
    }

    /**
     * Checks if the start of the interval is unbounded.
     *
     * @return true if start is unbounded
     */
    public boolean isUnboundedStart() {
        return this.start.equals(Instant.MIN);
    }

    /**
     * Checks if the end of the interval is unbounded.
     *
     * @return true if end is unbounded
     */
    public boolean isUnboundedEnd() {
        return this.end.equals(Instant.MAX);
    }

    /**
     * Checks if this interval contains the specified instant.
     * <p>
     * This checks if the specified instant is within the bounds of this interval.
     * If this range has an unbounded start then {@code contains(Instant#MIN)} returns true.
     * If this range has an unbounded end then {@code contains(Instant#MAX)} returns true.
     * If this range is empty then this method always returns false.
     *
     * @param instant The instant, not null
     * @return true if this interval contains the instant
     */
    public boolean contains(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return this.start.compareTo(instant) <= 0 && (instant.compareTo(this.end) < 0 || isUnboundedEnd());
    }

    /**
     * Checks if this interval encloses the specified interval.
     * <p>
     * This checks if the bounds of the specified interval are within the bounds of this interval.
     * An empty interval encloses itself.
     *
     * @param other the other interval, not null
     * @return true if this interval contains the other interval
     */
    public boolean encloses(TimeInterval other) {
        Objects.requireNonNull(other, "other");
        return this.start.compareTo(other.getStart()) <= 0 && other.getEnd().compareTo(this.end) <= 0;
    }

    /**
     * Checks if this interval overlaps the specified interval.
     *
     * @param other the other interval, not null
     * @return true if this interval overlaps the other interval
     */
    public boolean overlaps(TimeInterval other) {
        Objects.requireNonNull(other, "other");
        return other.equals(this) || (start.compareTo(other.end) < 0 && other.start.compareTo(end) < 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof TimeInterval) {
            TimeInterval castedObj = (TimeInterval) obj;
            return this.start.equals(castedObj.getStart()) && this.end.equals(castedObj.getEnd());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("TimeInterval[%s, %s]", this.start.toString(), this.end.toString());
    }
}