package com.anon.ecom.item.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    private String title;

    private String developer;

    private Integer releaseYear;

    private Integer ageRequired;

    private String platform;

    private List<String> tags;

    private String imageUrl;

}