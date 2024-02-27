package com.beeline.beelineapplication.entities;

import com.beeline.beelineapplication.inspectors.LogInspector;
import com.beeline.beelineapplication.constants.Categories;

import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.Date;
import java.util.UUID;

public final class Product extends LogInspector {
    public long getPrice() {
        return this.price;
    }

    public void setPrice ( final long price ) {
        this.price = price;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount ( final int totalCount ) {
        this.totalCount = totalCount;
    }

    public int getProductWasSoldCount() {
        return this.productWasSoldCount;
    }

    public void setProductWasSoldCount ( final int productWasSoldCount ) {
        this.productWasSoldCount = productWasSoldCount;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProductName() {
        return this.productName;
    }

    public UUID getId() {
        return this.id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public Categories getCategory() {
        return this.category;
    }

    private long price;

    // количество оставшихся товаров в хранилице
    private int totalCount;

    // количество проданных товаров
    private int productWasSoldCount;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public void setId ( final UUID id ) {
        this.id = id;
    }

    public void setCreatedDate ( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    private String description; // описание товара
    private String productName; // название товара

    private UUID id;

    // дата создания товара
    private Date createdDate;

    private Categories category = Categories.FOR_MEN;

    public static Product generate (
            final ResultSet resultSet
    ) {
        return new Product( resultSet );
    }

    private Product (
            final ResultSet resultSet
    ) {
        try {
            this.setId( resultSet.getObject( "id", UUID.class ) );

            this.setProductName( resultSet.getString( "productName" ) );
            this.setDescription( resultSet.getString( "description" ) );

            this.setCreatedDate( resultSet.getDate( "created_date" ) );
            this.setCategory( resultSet.getObject( "category", Categories.class ) );
        } catch ( final SQLException exception ) {
            super.logging( exception );
        }
    }
}
