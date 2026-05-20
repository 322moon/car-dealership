package org.example.storage.stock;

public record ReservationResult(boolean successful, String failReason) {
    public static ReservationResult success() {
        return new ReservationResult(true, null);
    }

    public static ReservationResult fail(String failReason) {
        return new ReservationResult(false, failReason);
    }
}
