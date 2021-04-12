package io.goolean.tech.hawker.sales.Lib.ImageCompression.imageCompression;

public interface ImageCompressionListener {
    void onStart();

    void onCompressed(String filePath);
}
