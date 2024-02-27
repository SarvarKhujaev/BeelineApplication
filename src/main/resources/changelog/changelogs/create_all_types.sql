CREATE TYPE entities_types.product AS (
      category entities_enums.categories,
      description TEXT,
      productName TEXT,
      price INT8,
      totalCount INT8,
      productWasSoldCount INT8
);