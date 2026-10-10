package com.example.stroy.entity;

public enum OrderStatus {
    NEW, CONFIRMED, SHIPPED, DELIVERED, CANCELLED;

    /** Buyurtma holatlari orasidagi ruxsat etilgan o'tishlar. */
    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case NEW -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == SHIPPED || next == CANCELLED;
            case SHIPPED -> next == DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }
}
