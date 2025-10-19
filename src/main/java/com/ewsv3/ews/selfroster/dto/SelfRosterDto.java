package com.ewsv3.ews.selfroster.dto;

import java.util.List;

public record SelfRosterDto(
                SelfRoster selfRoster,
                List<SelfRosterLine> selfRosterLine

) {

}
