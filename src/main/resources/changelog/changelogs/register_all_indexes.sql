CREATE INDEX IF NOT EXISTS entities_patruls_index ON entities.products
    ( category NULLS LAST );

CREATE INDEX IF NOT EXISTS entities_patruls_index ON entities.orders
    ( userId NULLS LAST, orderStatus NULLS LAST );
