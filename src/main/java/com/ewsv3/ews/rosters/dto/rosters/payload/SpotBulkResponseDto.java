package com.ewsv3.ews.rosters.dto.rosters.payload;

import java.util.List;

/**
 * Response of {@code POST /api/roster/spot-bulk}: one result per entry, in request order.
 */
public record SpotBulkResponseDto(int successCount, int failedCount, List<SpotBulkResult> results) {
}
