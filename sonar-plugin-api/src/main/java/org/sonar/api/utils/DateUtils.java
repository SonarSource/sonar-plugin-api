/*
 * SonarQube
 * Copyright (C) 2009-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.api.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import static org.sonar.api.utils.Preconditions.checkArgument;

/**
 * Parses and formats <a href="https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html#rfc822timezone">RFC 822</a> dates.
 * This class is thread-safe.
 *
 * @since 2.7
 */
public final class DateUtils {
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

  private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

  private DateUtils() {
  }

  /**
   * Warning: relies on default timezone!
   */
  public static String formatDate(Date d) {
    return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
  }

  /**
   * Warning: relies on default timezone!
   *
   * @since 7.6
   */
  public static String formatDate(Instant d) {
    return d.atZone(ZoneId.systemDefault()).toLocalDate().toString();
  }

  /**
   * Warning: relies on default timezone!
   */
  public static String formatDateTime(Date d) {
    return formatDateTime(OffsetDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault()));
  }

  /**
   * Warning: relies on default timezone!
   */
  public static String formatDateTime(long ms) {
    return formatDateTime(OffsetDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault()));
  }

  /**
   * @since 6.6
   */
  public static String formatDateTime(OffsetDateTime dt) {
    return DATETIME_FORMATTER.format(dt);
  }

  /**
   * Warning: relies on default timezone!
   */
  public static String formatDateTimeNullSafe(@Nullable Date date) {
    return date == null ? "" : formatDateTime(date);
  }

  @CheckForNull
  public static Date longToDate(@Nullable Long time) {
    return time == null ? null : new Date(time);
  }

  @CheckForNull
  public static Long dateToLong(@Nullable Date date) {
    return date == null ? null : date.getTime();
  }

  /**
   * Return a date at the start of day.
   *
   * @param s string in format {@link #DATE_FORMAT}
   * @throws SonarException when string cannot be parsed
   */
  public static Date parseDate(String s) {
    return Date.from(parseLocalDate(s).atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Parse format {@link #DATE_FORMAT}. This method never throws exception.
   *
   * @param s any string
   * @return the date, {@code null} if parsing error or if parameter is {@code null}
   * @since 3.0
   */
  @CheckForNull
  public static Date parseDateQuietly(@Nullable String s) {
    Date date = null;
    if (s != null) {
      try {
        date = parseDate(s);
      } catch (RuntimeException e) {
        // ignore
      }

    }
    return date;
  }

  /**
   * @since 6.6
   */
  public static LocalDate parseLocalDate(String s) {
    try {
      return LocalDate.parse(s);
    } catch (DateTimeParseException e) {
      throw MessageException.of("The date '" + s + "' does not respect format '" + DATE_FORMAT + "'", e);
    }
  }

  /**
   * Parse format {@link #DATE_FORMAT}. This method never throws exception.
   *
   * @param s any string
   * @return the date, {@code null} if parsing error or if parameter is {@code null}
   * @since 6.6
   */
  @CheckForNull
  public static LocalDate parseLocalDateQuietly(@Nullable String s) {
    LocalDate date = null;
    if (s != null) {
      try {
        date = parseLocalDate(s);
      } catch (RuntimeException e) {
        // ignore
      }

    }
    return date;
  }

  /**
   * @param s string in format {@link #DATETIME_FORMAT}
   * @throws SonarException when string cannot be parsed
   */
  public static Date parseDateTime(String s) {
    return Date.from(parseOffsetDateTime(s).toInstant());
  }

  /**
   * @param s string in format {@link #DATETIME_FORMAT}
   * @throws SonarException when string cannot be parsed
   * @since 6.6
   */
  public static OffsetDateTime parseOffsetDateTime(String s) {
    try {
      return OffsetDateTime.parse(s, DATETIME_FORMATTER);
    } catch (DateTimeParseException e) {
      throw MessageException.of("The date '" + s + "' does not respect format '" + DATETIME_FORMAT + "'", e);
    }
  }

  /**
   * Parse format {@link #DATETIME_FORMAT}. This method never throws exception.
   *
   * @param s any string
   * @return the datetime, {@code null} if parsing error or if parameter is {@code null}
   */
  @CheckForNull
  public static Date parseDateTimeQuietly(@Nullable String s) {
    Date datetime = null;
    if (s != null) {
      try {
        datetime = parseDateTime(s);
      } catch (RuntimeException e) {
        // ignore
      }

    }
    return datetime;
  }

  /**
   * Parse format {@link #DATETIME_FORMAT}. This method never throws exception.
   *
   * @param s any string
   * @return the datetime, {@code null} if parsing error or if parameter is {@code null}
   * @since 6.6
   */
  @CheckForNull
  public static OffsetDateTime parseOffsetDateTimeQuietly(@Nullable String s) {
    OffsetDateTime datetime = null;
    if (s != null) {
      try {
        datetime = parseOffsetDateTime(s);
      } catch (RuntimeException e) {
        // ignore
      }

    }
    return datetime;
  }

  /**
   * Warning: rely on default timezone!
   *
   * @see #parseDateOrDateTime(String, ZoneId) 
   * @since 6.1
   */
  @CheckForNull
  public static Date parseDateOrDateTime(@Nullable String stringDate) {
    return parseDateOrDateTime(stringDate, ZoneId.systemDefault());
  }

  /**
   * Parse either a full date time (using RFC-822 TZ format), or a local date.
   * For local dates, the returned {@link Date} will be set at the beginning of the day, in the provided timezone.
   * @return the datetime, {@code null} if stringDate is null
   * @throws IllegalArgumentException if stringDate is not a correctly formed date or datetime
   * @since 8.6
   */
  @CheckForNull
  public static Date parseDateOrDateTime(@Nullable String stringDate, ZoneId timeZone) {
    if (stringDate == null) {
      return null;
    }

    OffsetDateTime odt = parseOffsetDateTimeQuietly(stringDate);
    if (odt != null) {
      return Date.from(odt.toInstant());
    }

    LocalDate ld = parseLocalDateQuietly(stringDate);
    checkArgument(ld != null, "Date '%s' cannot be parsed as either a date or date+time", stringDate);

    return Date.from(ld.atStartOfDay(timeZone).toInstant());
  }

  /**
   * Warning: rely on default timezone for local dates!
   *
   * @see #parseDateOrDateTime(String)
   */
  @CheckForNull
  public static Date parseStartingDateOrDateTime(@Nullable String stringDate) {
    return parseDateOrDateTime(stringDate);
  }

  /**
   * @see #parseDateOrDateTime(String, ZoneId)
   */
  @CheckForNull
  public static Date parseStartingDateOrDateTime(@Nullable String stringDate, ZoneId timeZone) {
    return parseDateOrDateTime(stringDate, timeZone);
  }

  /**
   * Warning: rely on default timezone for local dates!
   *
   * @see #parseEndingDateOrDateTime(String, ZoneId)
   * @since 6.1
   */
  @CheckForNull
  public static Date parseEndingDateOrDateTime(@Nullable String stringDate) {
    return parseEndingDateOrDateTime(stringDate, ZoneId.systemDefault());
  }
  /**
   * Return the datetime if @param stringDate is a datetime, local date + 1 day if stringDate is a local date.
   * So '2016-09-01' would return a date equivalent to '2016-09-02T00:00:00' in the provided timezone
   *
   * @return the datetime, {@code null} if stringDate is null
   * @throws IllegalArgumentException if stringDate is not a correctly formed date or datetime
   * @since 8.6
   */
  @CheckForNull
  public static Date parseEndingDateOrDateTime(@Nullable String stringDate, ZoneId timeZone) {
    if (stringDate == null) {
      return null;
    }

    OffsetDateTime odt = parseOffsetDateTimeQuietly(stringDate);
    if (odt != null) {
      return Date.from(odt.toInstant());
    }

    LocalDate ld = parseLocalDateQuietly(stringDate);
    checkArgument(ld != null, "Date '%s' cannot be parsed as either a date or date+time", stringDate);

    return Date.from(ld.atStartOfDay(timeZone).plusDays(1).toInstant());
  }

  /**
   * Adds a number of days to a date returning a new object.
   * The original date object is unchanged.
   *
   * @param date         the date, not null
   * @param numberOfDays the amount to add, may be negative
   * @return the new date object with the amount added
   */
  public static Date addDays(Date date, int numberOfDays) {
    return Date.from(date.toInstant().plus(numberOfDays, ChronoUnit.DAYS));
  }

  /**
   * @since 7.6
   */
  public static Instant addDays(Instant instant, int numberOfDays) {
    return instant.plus(numberOfDays, ChronoUnit.DAYS);
  }

  @CheckForNull
  public static Date truncateToSeconds(@Nullable Date d) {
    if (d == null) {
      return null;
    }
    return truncateToSecondsImpl(d);
  }

  public static long truncateToSeconds(long dateTime) {
    return truncateToSecondsImpl(new Date(dateTime)).getTime();
  }

  private static Date truncateToSecondsImpl(Date d) {
    Instant instant = d.toInstant();
    instant = instant.truncatedTo(ChronoUnit.SECONDS);
    return Date.from(instant);
  }

}
