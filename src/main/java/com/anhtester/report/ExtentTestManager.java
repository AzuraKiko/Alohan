package com.anhtester.report;

import com.aventstack.extentreports.ExtentTest;

/**
 * Lớp ExtentTestManager chịu trách nhiệm quản lý các đối tượng ExtentTest
 * trong môi trường đa luồng. Nó sử dụng ThreadLocal để đảm bảo mỗi luồng
 * có một phiên bản ExtentTest riêng, tránh xung đột hoặc ghi đè dữ liệu
 * khi thực thi song song.
 */
public class ExtentTestManager {

    // Biến ThreadLocal để lưu trữ đối tượng ExtentTest cho mỗi luồng riêng biệt
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    /**
     * Lấy đối tượng ExtentTest hiện tại của luồng.
     *
     * @return Đối tượng ExtentTest được liên kết với luồng hiện tại.
     */
    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    /**
     * Thiết lập đối tượng ExtentTest cho luồng hiện tại.
     *
     * @param test Đối tượng ExtentTest sẽ được liên kết với luồng hiện tại.
     */
    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    /**
     * Xóa đối tượng ExtentTest khỏi ThreadLocal của luồng hiện tại.
     * Phương thức này nên được gọi sau khi kết thúc một bài kiểm tra để giải phóng tài nguyên.
     */
    public static void unload() {
        extentTest.remove();
    }

}

