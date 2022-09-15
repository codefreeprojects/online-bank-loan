package com.ol.bankloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor @Data @AllArgsConstructor
public class PaginationDTO {

    private int page;

    private int size;
}
