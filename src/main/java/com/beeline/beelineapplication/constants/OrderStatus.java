package com.beeline.beelineapplication.constants;

public enum OrderStatus {
    PAID, // ЗАКАЗ ОПЛАЧЕН
    CLOSED, // ЗАКАЗ ЗАКРЫТ ПОСЛЕ ПЕРЕДАЧИ КЛИЕНТУ И ОПЛАТЫ
    CREATED, // ЗАКАЗ СОЗДАН
    ARRIVED, // ЗАКАЗ ДОСТАВЛЕН
    CANCELED, // ЗАКАЗ ОТМЕНЕН
}
