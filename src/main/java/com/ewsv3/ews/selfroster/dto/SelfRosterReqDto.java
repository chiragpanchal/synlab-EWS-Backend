package com.ewsv3.ews.selfroster.dto;

import java.util.List;

public record SelfRosterReqDto(
                SelfRoster selfRoster,
                List<SelfRosterLine> selfRosterLines

) {

}
