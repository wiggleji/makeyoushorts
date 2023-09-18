package com.example.makeyoushorts.youtube.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class YoutubeBezierCurveDto {
    Integer videoLength;
    public ArrayList<Float> startPoint;
    public ArrayList<ArrayList<ArrayList<Float>>> bezierCurvePoints;
}
