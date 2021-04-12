package com.wajahatkarim3.longimagecamera;

public interface ImageCompressionListener {
    void onStart();

    void onCompressed(String filePath);
}
