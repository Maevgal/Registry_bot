DROP TABLE IF EXISTS outgoing_document;
DROP TABLE IF EXISTS tg_operation;

CREATE TABLE tg_operation
(
    id                      bigserial PRIMARY KEY,
    chat_id                 bigint UNIQUE  NOT NULL,
    outgoing_document_id    bigint,
    document_type           TEXT,
    status                  TEXT            NOT NULL,
    recipient               TEXT,
    sender                  TEXT,
    description             TEXT,
    copy_file               TEXT,
    create_document_date    TIMESTAMP,
    create_at               TIMESTAMP,
    update_at               TIMESTAMP
);

CREATE TABLE outgoing_document
(
    id                      bigserial PRIMARY KEY,
    number                  TEXT,
    create_document_date    TIMESTAMP   NOT NULL,
    recipient               TEXT        NOT NULL,
    sender                  TEXT        NOT NULL,
    description             TEXT        NOT NULL,
    copy_file               TEXT,
    create_at               TIMESTAMP,
    update_at               TIMESTAMP
);

alter table tg_operation
    add foreign key (outgoing_document_id) references outgoing_document;