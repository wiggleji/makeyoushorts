package com.example.makeyoushorts.youtube.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YoutubeVideoInfoDto {
    String ytpHeatMapPathD;
    Integer videoLength;
}
