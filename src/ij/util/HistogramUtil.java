package ij.util;

import ij.ImagePlus;
import ij.gui.HistogramConstants;
import ij.measure.Measurements;
import ij.plugin.filter.Analyzer;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import ij.process.ImageStatistics;

public class HistogramUtil implements Measurements {

    private HistogramUtil() {
    }

    public static boolean calculateLimitToThreshold(ImageProcessor ip) {
        boolean limitToThreshold = (Analyzer.getMeasurements() & LIMIT) != 0;
        if (ip.getMinThreshold() != ImageProcessor.NO_THRESHOLD
                && ip.getLutUpdateMode() == ImageProcessor.NO_LUT_UPDATE)
            limitToThreshold = false; // ignore invisible thresholds
        return limitToThreshold;
    }

    public static ImageStatistics getRGBHistogram(ImagePlus imp, int bins, double histMin, double histMax) {
        ImageProcessor ip = imp.getProcessor();
        ip = ip.crop();
        int w = ip.getWidth();
        int h = ip.getHeight();
        ImageProcessor ip2 = new ByteProcessor(w * 3, h);
        ByteProcessor temp = null;
        for (int i = 0; i < 3; i++) {
            temp = ((ColorProcessor) ip).getChannel(i + 1, temp);
            ip2.insert(temp, i * w, 0);
        }
        ImagePlus imp2 = new ImagePlus("imp2", ip2);
        return imp2.getStatistics(AREA + MEAN + MODE + MIN_MAX, bins, histMin, histMax);
    }

    public static ImageStatistics getStats(int rgbMode, ImagePlus imp, ImageProcessor ip, int bins,
            double histMin, double histMax) {

        rgbMode = imp.isRGB() && rgbMode < HistogramConstants.INTENSITY1 ? HistogramConstants.INTENSITY1 : rgbMode;

        if (HistogramUtil.isColor(rgbMode)) {
            int channel = rgbMode - 2;
            ColorProcessor cp = (ColorProcessor) imp.getProcessor();
            ip = cp.getChannel(channel, null);
            ImagePlus imp2 = new ImagePlus("", ip);
            imp2.setRoi(imp.getRoi());
            return imp2.getStatistics(AREA + MEAN + MODE + MIN_MAX, bins, histMin, histMax);
        } else if (HistogramUtil.isRGB(rgbMode))
            return HistogramUtil.getRGBHistogram(imp, bins, histMin, histMax);
        else
            return imp.getStatistics(
                    AREA + MEAN + MODE + MIN_MAX
                            + (HistogramUtil.calculateLimitToThreshold(imp.getProcessor()) ? LIMIT : 0),
                    bins, histMin,
                    histMax);
    }

    public static boolean isColor(int rgbMode) {
        return rgbMode == HistogramConstants.RED || rgbMode == HistogramConstants.GREEN
                || rgbMode == HistogramConstants.BLUE;
    }

    public static boolean isRGB(int rgbMode) {
        return rgbMode == HistogramConstants.RGB;
    }

}