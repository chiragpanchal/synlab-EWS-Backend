package com.ewsv3.ews.rosters.dto.rosters.payload;

/**
 * Outcome of one {@link SpotBulkEntry}.
 *
 * @param ref          the entry's {@code ref}
 * @param status       "S" (success) or "E" (error)
 * @param createdCount schedules created for the entry (only meaningful when status is "S")
 * @param message      error message when status is "E"
 */
public record SpotBulkResult(Integer ref, String status, int createdCount, String message) {
}
