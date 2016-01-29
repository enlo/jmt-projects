select * from
(
    select work_query0.*, @{dialect.rowNumber} _row_number1,
    from( @{originalSql} ) work_query0
    where @{dialect.rowNumber} <= @{limit + offset}
) _row_number1 > @{offset}