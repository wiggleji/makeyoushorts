package com.example.makeyoushorts.youtube.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class YoutubeTopFiveHighlightsDto {
    public ArrayList<ArrayList<Integer>> topHighlights;
}
