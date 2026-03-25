package com.team2.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateIncotermRequest {
    private String incotermCode;
    private String incotermName;
    private String incotermNameKr;
    private String incotermDescription;
    private String incotermTransportMode;
    private String incotermSellerSegments;
    private String incotermDefaultNamedPlace;
}
