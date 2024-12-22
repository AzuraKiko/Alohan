package com.anhtester.Commons;

import java.io.File;

public class GlobalConstants {

    // Đường dẫn tới thư mục gốc của dự án, lấy từ System property "user.dir"
    public static final String PROJECT_PATH = System.getProperty("user.dir");

    // Phiên bản Java đang sử dụng, lấy từ System property "java.version"
    public static final String JAVA_VERSION = System.getProperty("java.version");

    // Đường dẫn lưu trữ ảnh chụp màn hình của ReportNG (hỗ trợ trong báo cáo test)
    public static final String REPORT_NG_SCREENSHOT =
            PROJECT_PATH + File.separator + "reportNGImages" + File.separator;

    public static final long SHORT_TIMEOUT = 1;
    public static final int THREE_SECONDS = 3;
    public static final long FIVE_SECONDS = 5;
    public static final long LONG_TIMEOUT = 30;

    // Số lần tăng click để thử lại khi thao tác không thành công
    public static final int CLICK_INCREASE = 4;

    public static final String ALOHAN_DEV_ENV = "https://alohan.bigmouth.com.vn/";
    public static final String ALOHAN_PROD_ENV = "https://alohan.com.vn/";
}

