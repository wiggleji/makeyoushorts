package com.example.makeyoushorts.youtube;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class CubicBezierCurveExtractorTest {

    private final String VALID_TEST_HTML = "M 0.0,100.0 C 1.0,86.2 2.0,33.0 5.0,31.0 C 8.0,29.0 11.0,78.2 15.0,90.0 C 19.0,101.8 21.0,90.0 25.0,90.0 C 29.0,90.0 31.0,90.0 35.0,90.0 C 39.0,90.0 41.0,90.0 45.0,90.0 C 49.0,90.0 51.0,90.0 55.0,90.0 C 59.0,90.0 61.0,90.0 65.0,90.0 C 69.0,90.0 71.0,90.0 75.0,90.0 C 79.0,90.0 81.0,90.0 85.0,90.0 C 89.0,90.0 91.0,90.0 95.0,90.0 C 99.0,90.0 101.0,90.0 105.0,90.0 C 109.0,90.0 111.0,90.0 115.0,90.0 C 119.0,90.0 121.0,90.0 125.0,90.0 C 129.0,90.0 131.0,90.0 135.0,90.0 C 139.0,90.0 141.0,90.0 145.0,90.0 C 149.0,90.0 151.0,90.0 155.0,90.0 C 159.0,90.0 161.0,90.0 165.0,90.0 C 169.0,90.0 171.0,90.0 175.0,90.0 C 179.0,90.0 181.0,90.0 185.0,90.0 C 189.0,90.0 191.0,90.0 195.0,90.0 C 199.0,90.0 201.0,90.0 205.0,90.0 C 209.0,90.0 211.0,90.5 215.0,90.0 C 219.0,89.5 221.0,87.8 225.0,87.7 C 229.0,87.6 231.0,89.1 235.0,89.5 C 239.0,90.0 241.0,89.9 245.0,90.0 C 249.0,90.1 251.0,90.0 255.0,90.0 C 259.0,90.0 261.0,90.0 265.0,90.0 C 269.0,90.0 271.0,90.0 275.0,90.0 C 279.0,90.0 281.0,90.0 285.0,90.0 C 289.0,90.0 291.0,91.4 295.0,90.0 C 299.0,88.6 301.0,86.8 305.0,82.9 C 309.0,78.9 311.0,71.2 315.0,70.2 C 319.0,69.3 321.0,75.4 325.0,78.3 C 329.0,81.2 331.0,82.8 335.0,84.6 C 339.0,86.4 341.0,86.5 345.0,87.3 C 349.0,88.1 351.0,88.5 355.0,88.8 C 359.0,89.1 361.0,89.0 365.0,88.8 C 369.0,88.5 371.0,88.8 375.0,87.6 C 379.0,86.3 381.0,82.8 385.0,82.6 C 389.0,82.4 391.0,85.2 395.0,86.7 C 399.0,88.1 401.0,89.3 405.0,90.0 C 409.0,90.7 411.0,90.0 415.0,90.0 C 419.0,90.0 421.0,90.0 425.0,90.0 C 429.0,90.0 431.0,90.0 435.0,90.0 C 439.0,90.0 441.0,92.6 445.0,90.0 C 449.0,87.4 451.0,80.3 455.0,77.0 C 459.0,73.7 461.0,72.3 465.0,73.6 C 469.0,74.8 471.0,80.8 475.0,83.2 C 479.0,85.5 481.0,84.7 485.0,85.3 C 489.0,86.0 491.0,86.3 495.0,86.4 C 499.0,86.5 501.0,85.9 505.0,85.6 C 509.0,85.3 511.0,88.1 515.0,84.9 C 519.0,81.6 521.0,71.5 525.0,69.4 C 529.0,67.3 531.0,71.9 535.0,74.4 C 539.0,76.9 541.0,80.3 545.0,81.9 C 549.0,83.6 551.0,82.4 555.0,82.8 C 559.0,83.1 561.0,83.6 565.0,83.6 C 569.0,83.6 571.0,84.8 575.0,82.9 C 579.0,80.9 581.0,75.6 585.0,73.8 C 589.0,72.0 591.0,72.2 595.0,73.7 C 599.0,75.1 601.0,79.4 605.0,81.2 C 609.0,82.9 611.0,83.2 615.0,82.3 C 619.0,81.4 621.0,77.8 625.0,76.5 C 629.0,75.1 631.0,77.5 635.0,75.5 C 639.0,73.5 641.0,66.4 645.0,66.3 C 649.0,66.3 651.0,88.5 655.0,75.2 C 659.0,62.0 661.0,-0.3 665.0,0.0 C 669.0,0.3 671.0,62.7 675.0,76.6 C 679.0,90.5 681.0,69.6 685.0,69.5 C 689.0,69.3 691.0,75.5 695.0,75.8 C 699.0,76.2 701.0,71.2 705.0,71.0 C 709.0,70.8 711.0,73.7 715.0,74.8 C 719.0,75.8 721.0,76.5 725.0,76.2 C 729.0,75.9 731.0,75.8 735.0,73.3 C 739.0,70.9 741.0,67.9 745.0,63.8 C 749.0,59.7 751.0,54.2 755.0,52.8 C 759.0,51.4 761.0,54.2 765.0,56.9 C 769.0,59.6 771.0,63.3 775.0,66.4 C 779.0,69.5 781.0,71.3 785.0,72.5 C 789.0,73.7 791.0,71.8 795.0,72.3 C 799.0,72.8 801.0,74.6 805.0,75.0 C 809.0,75.3 811.0,75.5 815.0,73.9 C 819.0,72.4 821.0,67.1 825.0,67.2 C 829.0,67.4 831.0,71.7 835.0,74.8 C 839.0,77.9 841.0,80.8 845.0,82.6 C 849.0,84.3 851.0,83.1 855.0,83.5 C 859.0,83.9 861.0,84.2 865.0,84.4 C 869.0,84.6 871.0,84.7 875.0,84.5 C 879.0,84.3 881.0,85.6 885.0,83.3 C 889.0,81.0 891.0,75.6 895.0,72.9 C 899.0,70.3 901.0,70.2 905.0,70.0 C 909.0,69.9 911.0,71.3 915.0,72.2 C 919.0,73.1 921.0,73.3 925.0,74.6 C 929.0,76.0 931.0,77.5 935.0,78.9 C 939.0,80.3 941.0,81.5 945.0,81.8 C 949.0,82.0 951.0,81.1 955.0,80.1 C 959.0,79.1 961.0,77.4 965.0,76.7 C 969.0,76.0 971.0,75.3 975.0,76.6 C 979.0,78.0 981.0,86.8 985.0,83.3 C 989.0,79.8 992.0,64.0 995.0,59.2 C 998.0,54.4 999.0,51.0 1000.0,59.2 C 1001.0,67.3 1000.0,91.8 1000.0,100.0";

    private final CubicBezierCurveExtractor cubicBezierCurveExtractor;

    @Autowired
    CubicBezierCurveExtractorTest(CubicBezierCurveExtractor cubicBezierCurveExtractor) {
        this.cubicBezierCurveExtractor = cubicBezierCurveExtractor;
    }

    @Test
    public void isValidBezierElement_returnsTrue_whenHtmlIsValid() {
        // given

        // when
        boolean validBezierElement = cubicBezierCurveExtractor.isValidBezierElement(VALID_TEST_HTML);

        // then
        Assertions.assertThat(validBezierElement).isTrue();
    }

    @Test
    public void isValidBezierElement_returnsFalse_whenHtmlIsNotValid() {
        // given
        String wrongHtml = "M 1.0,999.0 C 105.0,2.0 102.0,29.0 11.0,78.2";

        // when
        boolean validBezierElement = cubicBezierCurveExtractor.isValidBezierElement(wrongHtml);

        // then
        Assertions.assertThat(validBezierElement).isFalse();
    }

    @Test
    public void getBezierCurvePoints_returnsBezierCurvePoints_lengthIsEqualCCommandString() {
        // given

        // when
        ArrayList<ArrayList<ArrayList<Float>>> bezierCurvePoints = cubicBezierCurveExtractor.getBezierCurvePoints(VALID_TEST_HTML);

        // then
        Assertions.assertThat(bezierCurvePoints.size()).isEqualTo(VALID_TEST_HTML.split("C").length - 1);
    }
    
    @Test
    public void getBezierStartPoint_returnsStartPoint_pointIsEqualMoveToCommand() {
        // given

        // when
        ArrayList<Float> startPoint = cubicBezierCurveExtractor.getBezierStartPoint(VALID_TEST_HTML);

        // then
        Assertions.assertThat(startPoint.get(0)).isEqualTo(0.0F);
        Assertions.assertThat(startPoint.get(1)).isEqualTo(100.0F);
    }
}