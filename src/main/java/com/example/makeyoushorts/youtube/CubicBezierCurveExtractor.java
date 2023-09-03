package com.example.makeyoushorts.youtube;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CubicBezierCurveExtractor {

    public boolean isValidBezierElement(String html) {
        // YouTube heatmap SVG Bezier points
        // always starts with M 0.0,100.0 and ends with 1000.0,100.0
        return html.startsWith("M 0.0,100.0") && html.endsWith("1000.0,100.0");
    }

    public ArrayList<Float> getBezierStartPoint(String html) {
        String[] bezierElements = html.split("C");
        return new ArrayList<>(convertStringArrayToFloatArray(bezierElements[0].replace("M ", "").split(",")));
    }

    public ArrayList<ArrayList<ArrayList<Float>>> getBezierCurvePoints(String html) {
        ArrayList<ArrayList<ArrayList<Float>>> bezierArrayList = new ArrayList<ArrayList<ArrayList<Float>>>();

        // M 0.0, 100.0 C x1,y1 x2,y2 x,y C ...
        // split by `C` command
        String[] bezierElements = html.split("C");
        // Bezier point starts with index 1 on HTML
        for (int i = 1; i < bezierElements.length; i++) {
            List<String> bezierList = new ArrayList<>(Arrays.stream(bezierElements[i].split(" ")).toList());
            bezierList.removeAll(Arrays.asList("", null));

            // [[x1,y1], [x2,y2], [x,y]]
            ArrayList<ArrayList<Float>> bezierCurvePoint = new ArrayList<>(bezierList.stream().map(
                    el -> convertStringArrayToFloatArray(el.split(","))
            ).toList());
            bezierArrayList.add(bezierCurvePoint);
        }
        return bezierArrayList;
    }

    private ArrayList<Float> convertStringArrayToFloatArray(String[] strings) {
        return new ArrayList<Float>(Arrays.stream(strings).map(Float::valueOf).toList());
    }
}
