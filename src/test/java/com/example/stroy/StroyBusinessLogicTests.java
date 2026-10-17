package com.example.stroy;

import com.example.stroy.entity.OrderStatus;
import com.example.stroy.util.Slugs;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StroyBusinessLogicTests {

    @Test
    void testSlugify() {
        assertEquals("tsement-i-sukhie-smesi", Slugs.slugify("Цемент и сухие смеси"));
        assertEquals("kirpich-krasnyy-m150", Slugs.slugify("Кирпич красный М150"));
        assertEquals("armatura-a500s", Slugs.slugify("Арматура А500С"));
        assertEquals("item", Slugs.slugify("   "));
        assertEquals("item", Slugs.slugify(null));
    }

    @Test
    void testOrderStatusTransitions() {
        // NEW can go to CONFIRMED or CANCELLED
        assertTrue(OrderStatus.NEW.canTransitionTo(OrderStatus.CONFIRMED));
        assertTrue(OrderStatus.NEW.canTransitionTo(OrderStatus.CANCELLED));
        assertFalse(OrderStatus.NEW.canTransitionTo(OrderStatus.SHIPPED));
        assertFalse(OrderStatus.NEW.canTransitionTo(OrderStatus.DELIVERED));

        // CONFIRMED can go to SHIPPED or CANCELLED
        assertTrue(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.SHIPPED));
        assertTrue(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.CANCELLED));
        assertFalse(OrderStatus.CONFIRMED.canTransitionTo(OrderStatus.DELIVERED));

        // SHIPPED can go to DELIVERED
        assertTrue(OrderStatus.SHIPPED.canTransitionTo(OrderStatus.DELIVERED));
        assertFalse(OrderStatus.SHIPPED.canTransitionTo(OrderStatus.CANCELLED));

        // DELIVERED & CANCELLED cannot transition further
        assertFalse(OrderStatus.DELIVERED.canTransitionTo(OrderStatus.CANCELLED));
        assertFalse(OrderStatus.DELIVERED.canTransitionTo(OrderStatus.NEW));
        assertFalse(OrderStatus.CANCELLED.canTransitionTo(OrderStatus.NEW));
    }
}
